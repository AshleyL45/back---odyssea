package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class Country {

    @Min(value = 1, message = "Country ID must be greater than or equal to 1")
    private int id;

    @NotBlank(message = "Country name is required")
    @Size(max = 255, message = "Country name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Continent is required")
    @Size(max = 255, message = "Continent must not exceed 255 characters")
    private String continent;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount with up to 2 decimal places")
    private BigDecimal price;

    public Country() {}

    public Country(int id, String name, String continent, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.price = price;
    }

    // Getters and Setters
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
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
