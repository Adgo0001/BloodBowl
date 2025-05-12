package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/set-username")
    public String showSetUsernamePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.findOrCreateUser(principal);

        // Hvis brugeren ikke eksisterer, redirect til forsiden
        if (user == null) {
            return "redirect:/";
        }

        // Hvis brugeren ikke har et brugernavn, vis muligheden for at vælge et
        if (user.getUsername() == null) {
            model.addAttribute("user", user);
            return "set-username"; // Vis formularen til at vælge et brugernavn
        }

        // Hvis brugeren allerede har et brugernavn, send dem til forsiden
        return "redirect:/";
    }

    @PostMapping("/set-username")
    public String setUsername(@RequestParam String username, @AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = userService.findOrCreateUser(principal);

        // Tjek om brugernavnet er tomt
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Brugernavnet må ikke være tomt.");
            return "set-username";
        }

        // Tjek om brugernavnet allerede er i brug
        Optional<User> userWithSameUsername = userService.findByUsername(username);
        if (userWithSameUsername.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Brugernavnet er allerede taget.");
            return "set-username";
        }

        // Log brugernavnet før opdatering
        System.out.println("Opdaterer brugernavn: " + username);

        // Opdater brugernavnet
        userService.updateUsername(user, username);
        return "redirect:/"; // Redirect til forsiden
    }
}
