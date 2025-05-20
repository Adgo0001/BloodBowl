package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/set-username")
    public String showSetUsernamePage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // eller redirect til forsiden
        }

        User user = userService.findOrCreateUser(authentication);

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

        User user = userService.findOrCreateUser(authentication);

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

        System.out.println("Opdaterer brugernavn: " + username);

        userService.updateUsername(user, username);
        return "redirect:/";
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";  // Path til list.html
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/form";  // Path til form.html
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/users/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

}