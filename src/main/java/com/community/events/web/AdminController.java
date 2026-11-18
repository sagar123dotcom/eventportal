package com.community.events.web;

import com.community.events.config.AdminAuthInterceptor;
import com.community.events.model.Event;
import com.community.events.service.EventService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService service;

    @Value("${portal.admin.username}")
    private String adminUser;

    @Value("${portal.admin.password}")
    private String adminPass;

    public AdminController(EventService service) {
        this.service = service;
    }

    // ---------- login / logout ----------
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, Model model) {
        if (adminUser.equals(username) && adminPass.equals(password)) {
            session.setAttribute(AdminAuthInterceptor.SESSION_KEY, username);
            return "redirect:/admin";
        }
        model.addAttribute("error", "Invalid username or password");
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ---------- event management ----------
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("events", service.findAll());
        return "admin/events";
    }

    @GetMapping("/events/new")
    public String newForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/form";
    }

    @GetMapping("/events/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", service.get(id));
        return "admin/form";
    }

    @PostMapping("/events")
    public String save(@Valid @ModelAttribute("event") Event event, BindingResult result,
                       RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "admin/form";
        }
        service.save(event);
        flash.addFlashAttribute("success", "Event saved.");
        return "redirect:/admin";
    }

    @PostMapping("/events/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        service.delete(id);
        flash.addFlashAttribute("success", "Event deleted.");
        return "redirect:/admin";
    }

    @GetMapping("/events/{id}/registrations")
    public String registrations(@PathVariable Long id, Model model) {
        model.addAttribute("event", service.get(id));
        return "admin/registrations";
    }
}
