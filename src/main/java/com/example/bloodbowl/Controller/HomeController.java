package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            try {
                User user = userService.findOrCreateUser(authentication);

                if (user != null && user.getUsername() == null) {
                    return "redirect:/set-username";
                }

                model.addAttribute("name", user.getName());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("username", user.getUsername());
                model.addAttribute("picture", user.getPicture());

            } catch (Exception e) {
                model.addAttribute("error", "Der opstod en fejl: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "Du er ikke logget ind.");
        }

        return "index";
    }
}