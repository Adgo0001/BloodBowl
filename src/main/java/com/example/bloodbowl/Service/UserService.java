package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Provider;
import com.example.bloodbowl.Model.Role;
import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Tilføjet da du bruger den i controlleren, men mangler i din kode:
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void registerUser(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Bruger findes allerede");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(Role.USER);
        user.setProvider(Provider.FORM);
        userRepository.save(user);
    }

    public User updateUsername(User user, String username) {
        user.setUsername(username);
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserRole(Long userId, Role newRole) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setRole(newRole);
            userRepository.save(user);
        });
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findOrCreateUser(Authentication auth) {
        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();
        String email = null;
        String name = null;
        String picture = null;

        if (principal instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) principal;
            Map<String, Object> attributes = oauthUser.getAttributes();

            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            picture = (String) attributes.get("picture");
        }

        if (email == null) {
            // Kan ikke finde email, returner null eller kast exception
            return null;
        }

        // Find bruger i databasen
        Optional<User> optionalUser = findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            // Opret ny bruger hvis ikke findes
            User user = new User();
            user.setEmail(email);
            user.setName(name != null ? name : "No Name");
            user.setPicture(picture);
            user.setRole(Role.USER); // standard rolle
            user.setProvider(Provider.GOOGLE); // eller andet ud fra login

            return createUser(user);
        }
    }
}