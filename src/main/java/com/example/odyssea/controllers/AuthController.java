package com.example.odyssea.controllers;

import com.example.odyssea.daos.userAuth.UserDao;
import com.example.odyssea.security.JwtToken;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public AuthController(AuthenticationManager authenticationManager, UserDao userDao, PasswordEncoder encoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
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
        return isUserSaved ? ResponseEntity.ok("User registered successfully!") : ResponseEntity.badRequest().body("Error: User registration failed.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user, HttpServletResponse response) {
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

            // Générer le JWT
            JwtToken jwtToken = jwtUtils.generateTokenWithId(userWithId.getId());

            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken.getToken())
                    .httpOnly(true)
                    .secure(false)         // A changer si Https
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ofSeconds(jwtExpirationMs))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            // Retourner le token JWT
            return ResponseEntity.ok("Login successful.");

        } catch (BadCredentialsException ex) {
            // Identifiants invalides
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> changeUserInformation(@PathVariable int id, @RequestBody User user){
        User userToUpdate = userDao.update(id, user);
        if (userToUpdate != null) {
            return ResponseEntity.ok("Account successfully updated.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or update failed.");
        }
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> changeUserPassword(@PathVariable int id, @RequestBody Map<String, String> passwordRequest){
        String newPassword = passwordRequest.get("password");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }

        userDao.updatePassword(id, newPassword);
        return ResponseEntity.ok("Password successfully updated.");
    }

    @DeleteMapping("/{id}/deleteAccount")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        boolean isDeleted = userDao.delete(id);
        if(isDeleted){
            return ResponseEntity.ok("Account successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("Error: User deletion failed.");
        }
    }
}
