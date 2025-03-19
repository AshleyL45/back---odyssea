package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

public class Hotel {

    @Min(value = 1, message = "Hotel ID must be greater than or equal to 1")
    private int id;

    @Min(value = 1, message = "City ID must be greater than or equal to 1")
    private int cityId;

    @NotBlank(message = "Hotel name is required")
    @Size(max = 255, message = "Hotel name must not exceed 255 characters")
    private String name;

    @Min(value = 1, message = "Star rating must be at least 1")
    @Max(value = 5, message = "Star rating must be no more than 5")
    private int starRating;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount with up to 2 decimal places")
    private Double price;

    public Hotel() {}

    public Hotel(int id, int cityId, String name, int starRating, String description, Double price) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.starRating = starRating;
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
    public int getStarRating() {
        return starRating;
    }
    public void setStarRating(int starRating) {
        this.starRating = starRating;
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
