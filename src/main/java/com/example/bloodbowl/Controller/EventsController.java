package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.Event;
import com.example.bloodbowl.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/events")
public class EventsController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "admin/events/list";  // Thymeleaf template til liste af events
    }

    @GetMapping("/new")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/events/create";  // Thymeleaf template til oprettelse
    }

    @PostMapping
    public String saveEvent(@ModelAttribute Event event) {
        eventService.save(event);
        return "redirect:/admin/events";
    }
}
