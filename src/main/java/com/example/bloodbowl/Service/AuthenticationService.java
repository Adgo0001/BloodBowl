package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Provider;
import com.example.bloodbowl.Model.Role;
import com.example.bloodbowl.Model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserService userService;

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public User findOrCreateOAuthUser(OAuth2User oAuth2User, String registrationId) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) throw new IllegalArgumentException("Ingen e-mail fundet");

        return userService.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            String name = (String) oAuth2User.getAttribute("name");
            user.setName(Optional.ofNullable(name).orElse(email));
            user.setProviderId(oAuth2User.getAttribute("sub")); // "id" for Facebook
            user.setPicture(oAuth2User.getAttribute("picture"));
            user.setRole(Role.USER);

            switch (registrationId.toLowerCase()) {
                case "google" -> user.setProvider(Provider.GOOGLE);
                case "facebook" -> user.setProvider(Provider.FACEBOOK);
                default -> throw new IllegalStateException("Ukendt OAuth provider: " + registrationId);
            }

            return userService.createUser(user);
        });
    }

    public User getAuthenticatedUser(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token &&
                authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            return findOrCreateOAuthUser(oAuth2User, token.getAuthorizedClientRegistrationId());
        } else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userService.findByEmail(userDetails.getUsername()).orElseThrow(
                    () -> new IllegalStateException("Custom bruger ikke fundet")
            );
        } else {
            throw new IllegalStateException("Ukendt brugertype");
        }
    }
}