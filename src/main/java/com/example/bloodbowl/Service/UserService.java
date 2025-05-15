package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Role;
import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Model.Provider;
import com.example.bloodbowl.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findOrCreateUser(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Ingen bruger er logget ind.");
        }

        Object principal = authentication.getPrincipal();

        if (authentication instanceof OAuth2AuthenticationToken oauthToken &&
                principal instanceof OAuth2User oAuth2User) {

            String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // "google", "facebook"
            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                throw new IllegalArgumentException("Ingen e-mail fundet i OAuth2 brugerdata.");
            }

            return userRepository.findByEmail(email).orElseGet(() -> {
                String name = oAuth2User.getAttribute("name");
                if (name == null) name = email;

                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setProviderId(oAuth2User.getAttribute("sub")); // eller "id"
                newUser.setPicture(oAuth2User.getAttribute("picture"));
                newUser.setRole(Role.USER);

                // TILDEL ENUM baseret på registrationId
                switch (registrationId.toLowerCase()) {
                    case "google" -> newUser.setProvider(Provider.GOOGLE);
                    case "facebook" -> newUser.setProvider(Provider.FACEBOOK);
                    default -> throw new IllegalStateException("Ukendt OAuth provider: " + registrationId);
                }

                return userRepository.save(newUser);
            });

        } else if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            return userRepository.findByEmail(email).orElseThrow(() ->
                    new IllegalStateException("Custom bruger findes ikke i databasen.")
            );

        } else {
            throw new IllegalStateException("Ukendt brugertype: " + principal.getClass().getName());
        }
    }


    public void registerUser(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Bruger findes allerede");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // krypter adgangskoden
        user.setName(name);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    // Metode til at opdatere brugernavn
    public User updateUsername(User user, String username) {
        user.setUsername(username);
        return userRepository.save(user);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}