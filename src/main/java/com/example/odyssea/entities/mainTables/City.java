package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

public class City {

    @Min(value = 1, message = "City ID must be greater than or equal to 1")
    private int id;

    @Min(value = 1, message = "Country ID must be greater than or equal to 1")
    private int countryId;

    @NotBlank(message = "City name is required")
    @Size(max = 255, message = "City name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "IATA code is required")
    @Size(max = 10, message = "IATA code must not exceed 10 characters")
    private String iataCode;

    @DecimalMin(value = "-180.0", inclusive = true, message = "Longitude must be at least -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "Longitude must be at most 180")
    private double longitude;

    @DecimalMin(value = "-90.0", inclusive = true, message = "Latitude must be at least -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "Latitude must be at most 90")
    private double latitude;

    public City() {}

    public City(int id, int countryId, String name, String iataCode, double longitude, double latitude) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
        this.iataCode = iataCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCountryId() {
        return countryId;
    }
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIataCode() {
        return iataCode;
    }
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
