package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import com.example.bloodbowl.model.PlayerResult;
import com.example.bloodbowl.service.AllServices;
import com.example.bloodbowl.service.CoachService;
import com.example.bloodbowl.service.PlayerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final AllServices allServices;
    private final CoachService coachService;
    private final PlayerResultService playerResultService;
    private final UserService userService;

    @Autowired
    public MainController(AllServices allServices, CoachService coachService, PlayerResultService playerResultService, UserService userService) {
        this.allServices = allServices;
        this.coachService = coachService;
        this.playerResultService = playerResultService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // Hent eller opret bruger ud fra auth info
            User user = userService.findOrCreateUser(auth);
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("picture", user.getPicture());
        }

        // Tilføj nyheder og events til model
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

    @GetMapping("/tournaments")
    public String tournaments() {
        return "tournaments";
    }
}
