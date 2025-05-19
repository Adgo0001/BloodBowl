package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Provider;
import com.example.bloodbowl.Model.User;
import com.example.bloodbowl.Repository.UserRepository;
import com.example.bloodbowl.Security.CustomUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Indlæser bruger med email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Bruger ikke fundet"));

        System.out.println("✅ Fundet bruger med rolle: " + user.getRole().name());

        if (user.getProvider() != Provider.FORM) {
            throw new BadCredentialsException("Bruger er registreret med " + user.getProvider() + ". Brug venligst " + user.getProvider().name().toLowerCase() + " login.");
        }

        return new CustomUserDetails(user); // <-- Bruger din egen klasse
    }
}
