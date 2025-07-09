package org.shop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // wyłącz CSRF do testów REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // pozwól na /auth/*
                        .anyRequest().authenticated()            // reszta wymaga uwierzytelnienia
                );
        return http.build();
    }
}
