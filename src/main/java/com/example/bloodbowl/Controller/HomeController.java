package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            try {
                User user = userService.findOrCreateUser(principal);

                // Hvis brugeren ikke har et brugernavn, send dem til set-username
                if (user != null && user.getUsername() == null) {
                    return "redirect:/set-username";  // Ompejling hvis brugernavnet er ikke sat
                }

                model.addAttribute("name", user.getName());
                model.addAttribute("email", user.getEmail());  // Tilføj email til model
                model.addAttribute("username", user.getUsername());
                model.addAttribute("picture", user.getPicture());  // Tilføj billede til model
            } catch (Exception e) {
                model.addAttribute("error", "Der opstod en fejl: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "Du er ikke logget ind.");
        }
        return "index";
    }
}