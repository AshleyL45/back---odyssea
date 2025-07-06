package com.example.odyssea.controllers;

import com.example.odyssea.dtos.UserName;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.security.JwtToken;
import com.example.odyssea.services.AuthService;
import com.example.odyssea.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Handles all operations related to logging in, registering and updating user's account information.")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided information."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok(
                ApiResponse.success("User successfully registered.", HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using email and password, returns a JWT."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtToken>> authenticateUser(@RequestBody User user) {//TODO Faire un DTO
        JwtToken token = authService.login(user);
        return ResponseEntity.ok(
                ApiResponse.success("User successfully logged in.",token, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Get connected user's name",
            description = "Returns the first name and last name of the currently authenticated user."
    )
    @PreAuthorize("isAuthenticated")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserName>> getConnectedUserName() {
        UserName userName = userService.getUserName();
        return ResponseEntity.ok(
                ApiResponse.success("User name successfully retrieved.", userName, HttpStatus.OK)
        );
    }
}
