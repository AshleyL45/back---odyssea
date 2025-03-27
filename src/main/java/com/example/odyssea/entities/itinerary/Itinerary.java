package com.example.odyssea.entities.itinerary;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class Itinerary {

    @Min(value = 1, message = "Itinerary ID must be greater than or equal to 1")
    private int id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotBlank(message = "Short description is required")
    @Size(max = 255, message = "Short description must not exceed 255 characters")
    private String shortDescription;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount with up to 2 decimal places")
    private BigDecimal price;

    @Min(value = 0, message = "Total duration must be greater than or equal to 0")
    private int totalDuration;

    @Min(value = 1, message = "Theme ID must be greater than or equal to 1")
    private int themeId;

    public Itinerary() {}

    public Itinerary(int id, String name, String description, String shortDescription, int stock, BigDecimal price, int totalDuration, int themeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.stock = stock;
        this.price = price;
        this.totalDuration = totalDuration;
        this.themeId = themeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
}
