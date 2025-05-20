package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import com.example.bloodbowl.Model.PlayerResult;
import com.example.bloodbowl.Service.AllServices;
import com.example.bloodbowl.Service.CoachService;
import com.example.bloodbowl.Service.PlayerResultService;
import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserService userService;
    private final CoachRepository coachRepository;

    @Autowired
    public MainController(AllServices allServices, CoachService coachService, PlayerResultService playerResultService, UserService userService, CoachRepository coachRepository) {
        this.allServices = allServices;
        this.coachService = coachService;
        this.playerResultService = playerResultService;
        this.userService = userService;
        this.coachRepository = coachRepository;
    }

    @GetMapping("/")
    public String homehome(Model model) {
        model.addAttribute("newsList", allServices.getAllNews());
        model.addAttribute("eventList", allServices.getAllEvents());
        return "index";
    }

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
