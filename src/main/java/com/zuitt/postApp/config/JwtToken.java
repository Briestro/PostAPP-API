package com.zuitt.postApp.config;

import com.zuitt.postApp.models.User;
import com.zuitt.postApp.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtToken implements Serializable {

    private static final long serialVersionUID =
            -434051048822997610L;

    // Set the token validity to 5 hours
    public static final long JWT_TOKEN_VALIDITY =
            5 * 60 * 60;

    // Get the JWT secret from the application properties
    @Value("${jwt.secret}")
    private String secret;

    private final UserRepository userRepository;

    // Inject UserRepository to access user information
    public JwtToken(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Remove "Bearer " if it is included in the token
    private String cleanToken(String token) {

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT token cannot be null or empty"
            );
        }

        token = token.trim();

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        return token;
    }

    // Create the key used to sign and verify the JWT
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Get the username stored inside the JWT
    public String getUsernameFromToken(String token) {

        token = cleanToken(token);

        return getClaimFromToken(
                token,
                Claims::getSubject
        );
    }

    // Get the expiration date stored inside the JWT
    public Date getExpirationDateFromToken(String token) {

        token = cleanToken(token);

        return getClaimFromToken(
                token,
                Claims::getExpiration
        );
    }

    // Get a specific piece of information from the JWT
    public <T> T getClaimFromToken(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        token = cleanToken(token);

        // Get all information stored in the token
        Claims claims =
                getAllClaimsFromToken(token);

        // Return the requested information
        return claimsResolver.apply(claims);
    }

    // Read and verify all information inside the JWT
    private Claims getAllClaimsFromToken(String token) {

        token = cleanToken(token);

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check if the JWT has already expired
    private boolean isTokenExpired(String token) {

        token = cleanToken(token);

        Date expiration =
                getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    // Create a new JWT for the authenticated user
    public String generateToken(
            UserDetails userDetails
    ) {

        Map<String, Object> claims =
                new HashMap<>();

        // Find the user using the username
        User user =
                userRepository.findByUsername(
                        userDetails.getUsername()
                );

        // Store the user's ID inside the JWT
        claims.put(
                "user",
                user.getId()
        );

        // Build the JWT using the user's information
        return doGenerateToken(
                claims,
                userDetails.getUsername()
        );
    }

    // Build, set the expiration, and sign the JWT
    private String doGenerateToken(
            Map<String, Object> claims,
            String subject
    ) {

        // Calculate when the token should expire
        long expirationTime =
                System.currentTimeMillis()
                        + JWT_TOKEN_VALIDITY * 1000;

        // Create and sign the JWT
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(
                        new Date(expirationTime)
                )
                .signWith(getSigningKey())
                .compact();
    }

    // Check if the JWT is valid
    public boolean validateToken(
            String token,
            UserDetails userDetails
    ) {

        try {

            // Clean the token first
            token = cleanToken(token);

            // Get the username from the token
            String username =
                    getUsernameFromToken(token);

            // Check username and token expiration
            return username.equals(
                    userDetails.getUsername()
            ) && !isTokenExpired(token);

        } catch (SignatureException e) {

            // Token signature does not match
            System.out.println(
                    "Invalid JWT signature"
            );

            return false;

        } catch (JwtException e) {

            // JWT is invalid or cannot be parsed
            System.out.println(
                    "Invalid JWT token"
            );

            return false;

        } catch (IllegalArgumentException e) {

            // Token is empty, null, or invalid
            System.out.println(
                    "JWT token is null, empty, or invalid"
            );

            return false;
        }
    }
}