package com.example.odyssea.entities.userAuth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class User {
    private int id;
    @NotBlank(message = "The email cannot be blank.")
    @Email(message = "The email must be valid.")
    private String email;
    @NotBlank(message = "Password cannot be blank.")
    private String password;
    private String role;
    @NotBlank(message = "Firstname cannot be blank.")
    private String firstName;
    @NotBlank(message = "Lastname cannot be blank.")
    private String lastName;

    public User() {
    }

    public User(int id, String email, String password, String role, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String password, String role, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
