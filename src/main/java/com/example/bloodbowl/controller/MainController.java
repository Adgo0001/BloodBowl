package com.example.bloodbowl.controller;

import com.example.bloodbowl.model.PlayerResult;
import com.example.bloodbowl.service.AllServices;
import com.example.bloodbowl.service.CoachService;
import com.example.bloodbowl.service.PlayerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final AllServices allServices;
    private final CoachService coachService;
    private final PlayerResultService playerResultService;

    @Autowired
    public MainController(AllServices allServices, CoachService coachService, PlayerResultService playerResultService) {
        this.allServices = allServices;
        this.coachService = coachService;
        this.playerResultService = playerResultService;
    }

    @GetMapping("/")
    public String homehome(Model model) {
        model.addAttribute("newsList", allServices.getAllNews());
        model.addAttribute("eventList", allServices.getAllEvents());
        return "index";
    }

    @GetMapping("/index")
    public String home(Model model) {
        model.addAttribute("newsList", allServices.getAllNews());
        model.addAttribute("eventList", allServices.getAllEvents());
        return "index";
    }

    @GetMapping("/archives")
    public String archives() {
        return "archives";
    }

    @GetMapping("/coach_rating")
    public String getCoachRating(Model model) {
        model.addAttribute("coachList", coachService.findAllCoaches());
        return "coach_rating";
    }

    @GetMapping("/danish_masters")
    public String danishMasters(Model model) {
        List<PlayerResult> results = playerResultService.getAllResults();
        model.addAttribute("results", results);
        return "danish_masters";
    }

    @GetMapping("/euro_bowl")
    public String euroBowl() {
        return "euro_bowl";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/tournaments")
    public String tournaments() {
        return "tournaments";
    }
}
