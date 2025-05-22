package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Provider;
import com.example.bloodbowl.Model.Role;
import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bruger ikke fundet med id: " + id));
    }

    public User createUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User createOAuthUser(String email, String name, String picture, String providerId, Provider provider) {
        User user = new User();
        user.setEmail(email);
        user.setName(name != null ? name : email);
        user.setPicture(picture);
        user.setProviderId(providerId);
        user.setProvider(provider);
        user.setRole(Role.USER);
        return createUser(user);
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

    public void updateUserRole(Long userId, Role newRole) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setRole(newRole);
            userRepository.save(user);
        });
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findOrCreateUser(Authentication auth) {
        if (auth == null) return null;

        Object principal = auth.getPrincipal();
        if (!(principal instanceof OAuth2User oauthUser)) return null;

        String email = oauthUser.getAttribute("email");
        if (email == null) return null;

        return findByEmail(email).orElseGet(() -> {
            String name = oauthUser.getAttribute("name");
            String picture = oauthUser.getAttribute("picture");
            String providerId = oauthUser.getAttribute("sub"); // Google / GitHub bruger "sub"
            return createOAuthUser(email, name, picture, providerId, Provider.GOOGLE);
        });
    }

    public User save(User user) {
        return createUser(user); // Ensartet entrypoint
    }
}