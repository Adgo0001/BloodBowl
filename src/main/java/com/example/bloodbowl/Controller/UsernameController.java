package com.example.bloodbowl.Controller;

import org.springframework.ui.Model;
import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.AuthenticationService;
import com.example.bloodbowl.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UsernameController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UsernameController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/set-username")
    public String showSetUsernamePage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = authenticationService.getAuthenticatedUser(authentication);

        if (user == null) {
            return "redirect:/";
        }

        if (user.getUsername() == null) {
            model.addAttribute("user", user);
            return "set-username";
        }

        return "redirect:/";
    }

    @PostMapping("/set-username")
    public String setUsername(@RequestParam String username, Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = authenticationService.getAuthenticatedUser(authentication);

        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Brugernavnet må ikke være tomt.");
            return "set-username";
        }

        Optional<User> userWithSameUsername = userService.findByUsername(username);
        if (userWithSameUsername.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Brugernavnet er allerede taget.");
            return "set-username";
        }

        userService.updateUsername(user, username);
        return "redirect:/";
    }
}