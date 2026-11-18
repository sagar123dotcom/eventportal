package com.community.events.service;

import com.community.events.model.Event;
import com.community.events.model.Registration;
import com.community.events.repo.EventRepository;
import com.community.events.repo.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {

    private final EventRepository events;
    private final RegistrationRepository registrations;

    public EventService(EventRepository events, RegistrationRepository registrations) {
        this.events = events;
        this.registrations = registrations;
    }

    public List<Event> searchUpcoming(String q) {
        return events.searchUpcoming(LocalDateTime.now(), q == null ? "" : q.trim());
    }

    public List<Event> findAll() {
        return events.findAllByOrderByEventDateDesc();
    }

    public Event get(Long id) {
        return events.findById(id).orElseThrow(() -> new NoSuchElementException("Event not found: " + id));
    }

    /** Create a new event or update an existing one (keeps its registrations). */
    @Transactional
    public Event save(Event form) {
        if (form.getId() == null) {
            return events.save(form);
        }
        Event existing = get(form.getId());
        existing.setTitle(form.getTitle());
        existing.setDescription(form.getDescription());
        existing.setLocation(form.getLocation());
        existing.setCategory(form.getCategory());
        existing.setEventDate(form.getEventDate());
        existing.setCapacity(form.getCapacity());
        return events.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        events.deleteById(id);
    }

    @Transactional
    public void register(Long eventId, String name, String email) {
        Event event = get(eventId);
        if (event.isPast()) {
            throw new IllegalStateException("This event has already taken place.");
        }
        if (event.isFull()) {
            throw new IllegalStateException("Sorry, this event is full.");
        }
        if (registrations.existsByEventIdAndEmailIgnoreCase(eventId, email.trim())) {
            throw new IllegalStateException("This email is already registered for the event.");
        }
        registrations.save(new Registration(event, name.trim(), email.trim()));
    }
}
