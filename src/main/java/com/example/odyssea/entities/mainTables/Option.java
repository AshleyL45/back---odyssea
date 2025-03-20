package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class Option {

    @Min(value = 1, message = "Option ID must be greater than or equal to 1")
    private int id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount with up to 2 decimal places")

    private BigDecimal price;
    private String category;


    public Option() {
    }

    public Option(int id, String name,String description, BigDecimal price, String category) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    // Getters & Setters
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

