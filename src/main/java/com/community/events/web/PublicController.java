package com.community.events.web;

import com.community.events.model.Event;
import com.community.events.model.RegistrationForm;
import com.community.events.service.EventService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicController {

    private final EventService service;

    public PublicController(EventService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false, defaultValue = "") String q, Model model) {
        model.addAttribute("events", service.searchUpcoming(q));
        model.addAttribute("q", q);
        return "index";
    }

    @GetMapping("/events/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("event", service.get(id));
        model.addAttribute("form", new RegistrationForm());
        return "event";
    }

    @PostMapping("/events/{id}/register")
    public String register(@PathVariable Long id,
                           @Valid @ModelAttribute("form") RegistrationForm form,
                           BindingResult result,
                           Model model,
                           RedirectAttributes flash) {
        Event event = service.get(id);
        if (!result.hasErrors()) {
            try {
                service.register(id, form.getName(), form.getEmail());
                flash.addFlashAttribute("success", "You're registered for " + event.getTitle() + "!");
                return "redirect:/events/" + id;
            } catch (IllegalStateException ex) {
                result.reject("registration.failed", ex.getMessage());
            }
        }
        model.addAttribute("event", event);
        return "event";
    }
}
