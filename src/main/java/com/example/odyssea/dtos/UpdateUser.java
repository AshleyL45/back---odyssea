package com.example.odyssea.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUser(
        @NotBlank(message = "The email cannot be blank.")
     @Email(message = "The email must be valid.")
     String email,

    @NotBlank(message = "Firstname cannot be blank.")
    String firstName,

    @NotBlank(message = "Lastname cannot be blank.")
    String lastName) {

}
