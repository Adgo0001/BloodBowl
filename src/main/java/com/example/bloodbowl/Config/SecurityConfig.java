package com.example.bloodbowl.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.bloodbowl.Config.CustomAuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(CustomAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow Danish Masters API endpoints
                        .requestMatchers("/api/**").permitAll()
                        // Allow static resources and favicon
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/**.jpg", "/**.png", "/favicon.ico").permitAll()
                        // Allow public pages
                        .requestMatchers("/", "/login", "/register", "/coach_rating", "/danish_masters", "/tournaments", "/euro_bowl", "/archives", "/**.css").permitAll()
                        .requestMatchers("/ApiTester", "/TournamentTracker", "/BloodbowlServletTest").permitAll()
                        // Require authentication for everything else
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler(failureHandler)
                        .defaultSuccessUrl("/set-username", true)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/set-username", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        // Disable CSRF for API endpoints only
                        .ignoringRequestMatchers("/api/**")
                )
                .headers(headers -> headers.frameOptions().disable()); // Keep your H2 config
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}