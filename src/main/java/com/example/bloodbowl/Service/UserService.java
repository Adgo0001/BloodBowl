package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(OAuth2User principal) {
        String email = principal.getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("Ingen e-mail fundet i OAuth2 brugerdata.");
        }

        try {
            return userRepository.findByEmail(email).orElseGet(() -> {
                String name = principal.getAttribute("name");
                if (name == null) name = email;

                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setProviderId(principal.getAttribute("sub")); // eller "id" for Facebook
                newUser.setPicture(principal.getAttribute("picture"));
                // Username = null indtil brugeren sætter det
                return userRepository.save(newUser);
            });
        } catch (Exception e) {
            throw new RuntimeException("Fejl ved oprettelse af bruger: " + e.getMessage());
        }
    }



    // Metode til at opdatere brugernavn
    public User updateUsername(User user, String username) {
        user.setUsername(username);
        return userRepository.save(user);  // Kontroller, om dette faktisk gemmer opdateringen
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}