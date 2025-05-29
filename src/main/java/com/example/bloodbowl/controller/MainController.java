package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Model.PlayerResult;
import com.example.bloodbowl.Repository.CoachRepository;
import com.example.bloodbowl.Service.AllServices;
import com.example.bloodbowl.Service.CoachService;
import com.example.bloodbowl.Service.PlayerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
    public String getCoachRating(Model model) {
        // Remove the old sorting logic for Coach entities
        // Add current year for Danish Masters functionality
        model.addAttribute("currentYear", LocalDate.now().getYear());
        return "coach_rating";  // This will load the Danish Masters functionality
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




    @GetMapping("/TournamentTracker")
    public String TournamentTracker() {
        return "TournamentTracker";
    }

    @GetMapping("/BloodbowlServletTest")
    public String BloodbowlServletTest() {
        return "BloodbowlServletTest";
    }

    @GetMapping("/euro_bowl")
    public String euroBowl() {
        return "euro_bowl";
    }

    @GetMapping("/ApiTester")
    public String ApiTester() {
        return "ApiTester";
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

