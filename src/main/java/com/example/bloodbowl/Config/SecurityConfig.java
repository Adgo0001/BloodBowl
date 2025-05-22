package com.example.bloodbowl.Config;

import com.example.bloodbowl.Repository.UserRepository;
import com.example.bloodbowl.Service.CustomUserDetailsService;
import com.example.bloodbowl.Service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;

    // Fjern CustomOAuth2UserService fra konstruktøren
    public SecurityConfig(CustomAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/**.jpg",
                                "/**.png",
                                "/coach_rating",
                                "/archives",
                                "/danish_masters",
                                "/euro_bowl",
                                "/tournaments",
                                "/**.css"
                        ).permitAll()
                        .requestMatchers("/admin_panel/**").hasRole("ADMIN")
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
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Korrekt injection her
                        )
                        .defaultSuccessUrl("/set-username", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}