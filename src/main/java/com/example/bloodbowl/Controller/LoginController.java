package com.example.bloodbowl.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Viser login-siden (login.html)
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Thymeleaf ser i templates/login.html
    }
}
