package com.example.bloodbowl.controller;

import com.example.bloodbowl.model.Coach;
import com.example.bloodbowl.model.PlayerResult;
import com.example.bloodbowl.repository.CoachRepository;
import com.example.bloodbowl.service.AllServices;
import com.example.bloodbowl.service.CoachService;
import com.example.bloodbowl.service.PlayerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private final AllServices allServices;
    private final CoachService coachService;
    private final PlayerResultService playerResultService;
    private final CoachRepository coachRepository;

    @Autowired
    public MainController(AllServices allServices, CoachService coachService, PlayerResultService playerResultService, CoachRepository coachRepository) {
        this.allServices = allServices;
        this.coachService = coachService;
        this.playerResultService = playerResultService;
        this.coachRepository = coachRepository;
    }

    @GetMapping("/")
    public String homehome(Model model) {
        model.addAttribute("newsList", allServices.getAllNews());
        model.addAttribute("eventList", allServices.getAllEvents());
        return "index";
    }

    @GetMapping("/archives")
    public String archives() {
        return "archives";
    }

    @GetMapping("/coach_rating")
    public String getCoachRating(
            @RequestParam(defaultValue = "winPercentage") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        System.out.println("Sort field: " + sortField + ", Sort dir: " + sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<Coach> coaches = coachRepository.findAll(Sort.by(direction, sortField));
        System.out.println("Antal coaches fundet: " + coaches.size());

        model.addAttribute("coachList", coaches);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "coach_rating";
    }

    @GetMapping("/danish_masters")
    public String danishMasters(
            @RequestParam(defaultValue = "totalScore") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<PlayerResult> results = playerResultService.getAllResultsSorted(Sort.by(direction, sortField));

        model.addAttribute("results", results);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "danish_masters";
    }


    @GetMapping("/euro_bowl")
    public String euroBowl() {
        return "euro_bowl";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String register() {
//        return "register";
//    }

    @GetMapping("/tournaments")
    public String tournaments() {
        return "tournaments";
    }
}
