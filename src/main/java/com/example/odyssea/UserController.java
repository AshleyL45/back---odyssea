package com.example.odyssea;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.UpdateUser;
import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Handles all operations related to the user account.")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Update account information",
            description = "Updates the user's account information (e.g., name, email)."
    )
    @PutMapping("/")
    public ResponseEntity<ApiResponse<Void>> changeUserInformation(@RequestBody UpdateUser updateRequest){
        userService.changeUserInformation(updateRequest);
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
