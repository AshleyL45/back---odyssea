package com.example.odyssea.services;

import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.exceptions.UserNotFoundException;
import com.example.odyssea.security.JwtToken;
import com.example.odyssea.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;;
    private final JwtUtil jwtUtils;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public AuthService(AuthenticationManager authenticationManager, UserDao userDao, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
    }

    public void login(@RequestBody User user, HttpServletResponse response){
        try {
            // Authentification avec Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    )
            );

            // Récupération des détails de l'utilisateur
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User userWithId = userDao.findByEmail(user.getEmail());

            JwtToken jwtToken = jwtUtils.generateToken(userWithId.getId());

            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken.getToken())
                    .httpOnly(true)
                    .secure(false)         // A changer si Https
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ofSeconds(jwtExpirationMs))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid username or password.");
        }

    }
}
