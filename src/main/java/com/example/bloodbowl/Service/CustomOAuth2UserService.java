package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthenticationService authenticationService;

    public CustomOAuth2UserService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        User user = authenticationService.findOrCreateOAuthUser(oauthUser, registrationId);

        // Authority: "ROLE_USER", "ROLE_ADMIN", etc.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                oauthUser.getAttributes(),
                "email" // bruges som unik nøgle i session
        );
    }
}