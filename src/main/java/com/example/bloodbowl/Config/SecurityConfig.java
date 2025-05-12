package com.example.bloodbowl.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()  // Tillad offentlig adgang til forsiden
                        .anyRequest().authenticated()                                      // Alt andet kræver login
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")        // URL for din custom login page
                        .defaultSuccessUrl("/set-username", true)  // Redirect til /set-username hvis login er succesfuldt
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")       // Redirect til / efter logout
                        .permitAll()
                );

        return http.build();
    }
}