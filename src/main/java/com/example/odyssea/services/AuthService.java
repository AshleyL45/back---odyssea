package com.example.odyssea.services;

import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.exceptions.UserNotFoundException;
import com.example.odyssea.security.JwtToken;
import com.example.odyssea.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;;
    private final JwtUtil jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, UserDao userDao, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
    }

    public JwtToken login(@RequestBody User user){
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

           return jwtUtils.generateToken(userWithId.getId(), userWithId.getRole());

        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid username or password : " + e);
        }

    }
}
