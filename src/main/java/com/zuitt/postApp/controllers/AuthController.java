package com.zuitt.postApp.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.zuitt.postApp.config.JwtToken;
import com.zuitt.postApp.models.JwtRequest;
import com.zuitt.postApp.models.JwtResponse;
import com.zuitt.postApp.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


// Marks this class as a REST Controller.
// Handles HTTP requests related to user authentication.
@RestController

// Allows requests from different origins/domains.
@CrossOrigin
public class AuthController {


    // AuthenticationManager is responsible for processing and verifying the user's authentication request.
    @Autowired
    private AuthenticationManager authenticationManager;


    // Injects the JwtToken Bean used to generate JWTs.
    @Autowired
    private JwtToken jwtToken;


    // Injects the service responsible for loading user information from the database.
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;


    // Maps POST requests sent to /authenticate to the createAuthenticationToken() method.
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)

    // Handles the user's login request and creates a JWT if the provided credentials are valid.
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtRequest authenticationRequest) throws Exception {


        // Get the username and password from the request and verify the user's credentials.
        authenticate(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );


        // Load the authenticated user's details using the username.
        final UserDetails userDetails =
                jwtUserDetailsService.loadUserByUsername(
                        authenticationRequest.getUsername()
                );


        // Generate a JWT using the authenticated user's details.
        final String token = jwtToken.generateToken(userDetails);


        // Return HTTP 200 (OK) with the generated JWT wrapped inside a JwtResponse object.
        return ResponseEntity.ok(new JwtResponse(token));
    }


    // Performs the actual authentication of the username and password.
    private void authenticate(String username, String password) throws Exception {

        try {

            // Create an authentication request containing
            // the username and password.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);


            // Ask Spring Security's AuthenticationManager
            // to verify the provided credentials.
            authenticationManager.authenticate(authenticationToken);


        } catch (DisabledException e) {

            // Thrown when the user's account is disabled.
            throw new Exception("USER_DISABLED", e);


        } catch (BadCredentialsException e) {

            // Thrown when the username or password is incorrect.
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
