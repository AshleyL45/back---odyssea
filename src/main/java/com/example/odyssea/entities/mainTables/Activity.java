package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Activity {

    @Min(value = 1, message = "Activity ID must be greater than or equal to 1")
    private int id;

    @Min(value = 1, message = "City ID must be greater than or equal to 1")
    private int cityId;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Type is required")
    @Size(max = 255, message = "Type must not exceed 255 characters")
    private String type;

    @NotBlank(message = "Physical effort description is required")
    @Size(max = 255, message = "Physical effort description must not exceed 255 characters")
    private String physicalEffort;

    @Min(value = 0, message = "Duration must be greater than or equal to 0")
    private LocalTime duration;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount with up to 2 decimal places")
    private Double price;

    public Activity() {}

    public Activity(int id, int cityId, String name, String type, String physicalEffort, LocalTime duration, String description, Double price) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.type = type;
        this.physicalEffort = physicalEffort;
        this.duration = duration;
        this.description = description;
        this.price = price;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPhysicalEffort() {
        return physicalEffort;
    }
    public void setPhysicalEffort(String physicalEffort) {
        this.physicalEffort = physicalEffort;
    }
    public LocalTime getDuration() {
        return duration;
    }
    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}
