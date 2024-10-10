package com.example.Rental.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabling CSRF for API requests, make sure you understand the security implications
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users").permitAll() // Allow public access to the /users endpoint
                        .anyRequest().authenticated() // All other requests must be authenticated
                )
                .httpBasic(withDefaults()); // Use this instead of deprecated `httpBasic()` method

        return http.build();
    }
}

