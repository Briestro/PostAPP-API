package com.zuitt.postApp.config;

import com.zuitt.postApp.services.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configures authentication, authorization, and JWT security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticate jwtAuthenticate;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // Configure how users are authenticated
    public void configureGlobal(
            AuthenticationManagerBuilder auth
    ) throws Exception {

        // Use JwtUserDetailsService to load user information
        // and BCrypt to check the password
        auth.userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    // Create the authentication error handler
    @Bean
    public static JwtAuthenticate jwtAuthenticationEntryPointBean()
            throws Exception {

        return new JwtAuthenticate();
    }

    // Create the password encoder
    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // Create the AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {

        return authenticationConfiguration
                .getAuthenticationManager();
    }

    // Configure the application's security rules
    @Bean
    protected SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        // Disable CSRF because the API uses JWT authentication
        http.csrf(csrf -> csrf.disable())

                // Return 401 when authentication fails
                .exceptionHandling(exp ->
                        exp.authenticationEntryPoint(
                                jwtAuthenticate
                        )
                )

                // Do not store authentication in a server session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Define which endpoints require authentication
                .authorizeHttpRequests(auth -> auth

                        // Login endpoint does not require a JWT
                        .requestMatchers("/authenticate")
                        .permitAll()

                        // Registration does not require a JWT
                        .requestMatchers("/users/register")
                        .permitAll()

                        // Anyone can view posts
                        .requestMatchers(
                                HttpMethod.GET,
                                "/posts"
                        ).permitAll()

                        // Allow browser preflight requests
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // All other requests require authentication
                        .anyRequest()
                        .authenticated()
                );

        // Check JWT before Spring's default authentication filter
        http.addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        // Build and return the security configuration
        return http.build();
    }
}