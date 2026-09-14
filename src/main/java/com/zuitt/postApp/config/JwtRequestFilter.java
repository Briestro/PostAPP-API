package com.zuitt.postApp.config;

import com.zuitt.postApp.services.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Runs once for every request to check the JWT
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Used to get user information from the database
    private final JwtUserDetailsService jwtUserDetailsService;

    // Used to read and validate the JWT
    private final JwtToken jwtTokenUtil;

    // Inject the required services
    public JwtRequestFilter(
            JwtUserDetailsService jwtUserDetailsService,
            JwtToken jwtTokenUtil
    ) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Main method that handles the JWT checking process
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        // Get the Authorization header from the request
        final String requestTokenHeader =
                request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        /*
         * Check if the request contains a Bearer token.
         *
         * Expected format:
         * Authorization: Bearer <JWT>
         */
        if (requestTokenHeader != null
                && requestTokenHeader.startsWith("Bearer ")) {

            // Remove "Bearer " and get the actual JWT
            jwtToken = requestTokenHeader
                    .substring(7)
                    .trim();

            try {

                // Get the username stored inside the JWT
                username =
                        jwtTokenUtil.getUsernameFromToken(jwtToken);

            } catch (ExpiredJwtException e) {

                // Token has already expired
                logger.warn("JWT Token has expired");

            } catch (IllegalArgumentException e) {

                // Token cannot be read properly
                logger.warn("Unable to get JWT Token");

            } catch (JwtException e) {

                // Token is invalid
                logger.warn("Invalid JWT Token");
            }

        } else if (requestTokenHeader != null) {

            // Authorization header exists but does not use Bearer
            logger.warn(
                    "JWT Token does not begin with Bearer"
            );
        }

        /*
         * Continue only if:
         *
         * 1. A username was successfully extracted.
         * 2. The user is not already authenticated.
         */
        if (username != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            // Get the user's information from the database
            UserDetails userDetails =
                    jwtUserDetailsService
                            .loadUserByUsername(username);

            /*
             * Check if the JWT is valid
             * for the retrieved user.
             */
            if (jwtTokenUtil.validateToken(
                    jwtToken,
                    userDetails
            )) {

                // Create an authenticated user
                UsernamePasswordAuthenticationToken
                        authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach request information to the authentication
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Store the authenticated user in Spring Security
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
        }

        // Continue processing the request
        chain.doFilter(request, response);
    }
}