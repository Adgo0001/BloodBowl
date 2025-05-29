package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Provider;
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

        String providerId = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "facebook" -> oAuth2User.getAttribute("id");
            default -> null;
        };

        // Her sikrer vi at name er defineret, hvis den ikke findes i OAuth2User, sættes den til email
        String name = oAuth2User.getAttribute("name");
        if (name == null) {
            name = email;
        }

        final String finalName = name;

        return userService.findByEmail(email).orElseGet(() ->
                userService.createOAuthUser(
                        email,
                        finalName,
                        oAuth2User.getAttribute("picture"),
                        providerId,
                        mapToProvider(registrationId)
                )
        );

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

    private Provider mapToProvider(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> Provider.GOOGLE;
            case "facebook" -> Provider.FACEBOOK;
            default -> throw new IllegalStateException("Ukendt provider: " + registrationId);
        };
    }
}