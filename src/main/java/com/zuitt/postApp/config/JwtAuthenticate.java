package com.zuitt.postApp.config;

// Used when an authentication error occurs
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;

// Handles requests that fail authentication
public class JwtAuthenticate implements AuthenticationEntryPoint, Serializable {

    // Used for serialization
    private static final long serialVersionUID = 5711985054996606983L;

    // This method is called when authentication fails
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        // Send HTTP 401 Unauthorized response to the client
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized"
        );
    }
}