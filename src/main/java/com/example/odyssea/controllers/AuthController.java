package com.example.odyssea.controllers;

import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.services.AuthService;
import com.example.odyssea.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            description = "Authenticates a user using email and password, returns a JWT in a cookie."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> authenticateUser(@RequestBody User user, HttpServletResponse response) {
        authService.login(user, response);
        return ResponseEntity.ok(
                ApiResponse.success("User successfully logged in.", HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Update account information",
            description = "Updates the user's account information (e.g., name, email)."
    )
    @PutMapping("/")
    public ResponseEntity<ApiResponse<Void>> changeUserInformation(@RequestBody User user){
        userService.changeUserInformation(user);
        return ResponseEntity.ok(
                ApiResponse.success("Account successfully updated.", HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Change user password",
            description = "Updates the user's password. The request must contain the new password."
    )
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changeUserPassword(@RequestBody Map<String, String> passwordRequest){
        String newPassword = passwordRequest.get("password");
        userService.changePassword(newPassword);

        return ResponseEntity.ok(
                ApiResponse.success("Password successfully updated.", HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Delete user account",
            description = "Deletes the authenticated user's account."
    )

    @DeleteMapping("/")
    public ResponseEntity<ApiResponse<Void>> deleteUser(){
        userService.deleteAccount();
        return ResponseEntity.ok(
                ApiResponse.success("Account successfully deleted.", HttpStatus.OK)
        );
    }
}
