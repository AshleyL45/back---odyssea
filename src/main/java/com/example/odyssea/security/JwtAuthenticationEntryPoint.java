package com.example.odyssea.security;

import jakarta.servlet.ServletException;                // <– jakarta, pas javax
import jakarta.servlet.http.HttpServletRequest;         // <– jakarta
import jakarta.servlet.http.HttpServletResponse;        // <– jakarta
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {          // <– on ajoute ServletException
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Not authorized : " + authException.getMessage());
    }
}
