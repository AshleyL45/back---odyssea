package com.example.odyssea.controllers;

import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.entities.userAuth.JwtToken;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.services.userAuth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserDao userDao, PasswordEncoder encoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        boolean alreadyExists = userDao.existsByEmail(user.getEmail());
        if (alreadyExists) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        User newUser = new User(
                user.getEmail(),
                encoder.encode(user.getPassword()),
                "USER",
                user.getFirstName(),
                user.getLastName()
        );
        boolean isUserSaved = userDao.save(newUser);
        return isUserSaved ? ResponseEntity.ok("User registered successfully!") : ResponseEntity.badRequest().body("Error: User registration failed!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userWithId = userDao.findByEmail(user.getEmail());
        JwtToken jwtToken = jwtUtils.generateToken(userDetails.getUsername(), userWithId.getId(), userWithId.getFirstName());
        return ResponseEntity.ok(jwtToken);
    }
}
