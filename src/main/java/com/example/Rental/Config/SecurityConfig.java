package com.example.Rental.Config;

import com.example.Rental.Services.UserServices.CustomOAuth2UserService;
import com.example.Rental.Services.UserServices.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/register", "/auth/verifyPhone","/api/admin/deleteUser/{userId}","/api/admin/updateProfile/{id}","/api/admin/addProfile","/api/user/updateProfile/{email}"
//                        ,"/api/claims/ADMIN/getAllClaims","/api/claims/USER/createClaim"
//                        ,"/api/claims/USER/{id}/updateDescription","/api/claims/COMPANY/{id}/REJECTED"
//                        ,"/api/claims/USER/{id}","/api/claims/ADMIN/status"
//                        ,"/api/claims/ADMIN/getAllClaims","/api/claims/COMPANY/totalClaimedAmount"
//                        ,"/api/claims/COMPANY/{id}/status"
//
//                        ,"/api/policies/USER/createPolicy"
//                        ,"/api/policies/ADMIN/getAllPolicies"
//                        ,"/api/policies/ADMIN/getPolicyById/{id}"
//                        ,"/api/policies/COMPANY/{id}/status"
//                        ,"/api/policies/COMPANY/deletePolicy/{id}"
//                        ,"/api/policies/USER/{id}/updateDetails"
//                        ,"/api/policies/ADMIN/findByStatus"
//                        ,"/api/policies/COMPANY/findByStatus"
//                        ,"/api/policies/COMPANY/{id}/updateCoverageAmount"
//                        ,"/api/policies/user/{userId}/policies").permitAll()
//                        .anyRequest().authenticated()
                                .requestMatchers("/**"
                                       ).permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                )
                .httpBasic(withDefaults());

        return http.build();
    }



    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

