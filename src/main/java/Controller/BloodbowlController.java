package Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class BloodbowlController {


    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Home");
        return "index";
    }


    @GetMapping("/archives")
    public String archives(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Archives");
        return "archives";
    }


    @GetMapping("/coach-rating")
    public String coachRating(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Coach Rating");
        return "coach_rating";
    }


    @GetMapping("/danish-masters")
    public String danishMasters(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Danish Masters");
        return "danish_masters";
    }


    @GetMapping("/euro-bowl")
    public String euroBowl(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Euro Bowl");
        return "euro_bowl";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Register");
        return "register";
    }


    @GetMapping("/tournaments")
    public String tournaments(Model model) {
        model.addAttribute("pageTitle", "Bloodbowl - Tournaments");
        return "tournaments";
    }
}