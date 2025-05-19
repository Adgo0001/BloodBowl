package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.Role;
import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin-panel")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel(Model model, Authentication authentication) {
        System.out.println("👤 Bruger forsøger at tilgå /admin-panel: " + authentication.getName());
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("👑 Bruger har rolle: " + authority.getAuthority());
        }

        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("roles", Role.values());
        return "admin-panel";
    }

    @PostMapping("/update-role")
    public String updateUserRole(@RequestParam Long userId, @RequestParam Role role) {
        userService.updateUserRole(userId, role);
        return "redirect:/admin-panel";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin-panel";
    }
}