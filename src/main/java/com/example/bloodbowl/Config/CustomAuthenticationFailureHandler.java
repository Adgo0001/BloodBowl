package com.example.bloodbowl.Config;

import com.example.bloodbowl.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository userRepository;

    public CustomAuthenticationFailureHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String email = request.getParameter("username");

        if (email != null && !userRepository.findByEmail(email).isPresent()) {
            // Vis login-siden igen, men med registreringsformular og forudfyldt email
            response.sendRedirect("/login?prefill=" + URLEncoder.encode(email, "UTF-8"));
        } else {
            // Hvis brugeren findes, men adgangskode er forkert
            response.sendRedirect("/login?error");
        }
    }
}