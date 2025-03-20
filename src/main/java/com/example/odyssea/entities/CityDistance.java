package com.example.odyssea.entities;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

public class CityDistance {

    private int id;

    @Min(value = 1, message = "From City ID must be greater than or equal to 1")
    private int fromCityId;

    @Min(value = 1, message = "To City ID must be greater than or equal to 1")
    private int toCityId;

    @Min(value = 0, message = "Driving duration (in seconds) must be greater than or equal to 0")
    private int drivingDurationSeconds;

    @DecimalMin(value = "0.0", inclusive = true, message = "Distance in km must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Distance in km must have at most 2 decimal places")
    private double distanceKm;

    public CityDistance() {}

    public CityDistance(int id, int fromCityId, int toCityId, int drivingDurationSeconds, double distanceKm) {
        this.id = id;
        this.fromCityId = fromCityId;
        this.toCityId = toCityId;
        this.drivingDurationSeconds = drivingDurationSeconds;
        this.distanceKm = distanceKm;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFromCityId() {
        return fromCityId;
    }
    public void setFromCityId(int fromCityId) {
        this.fromCityId = fromCityId;
    }

    public int getToCityId() {
        return toCityId;
    }
    public void setToCityId(int toCityId) {
        this.toCityId = toCityId;
    }

    public int getDrivingDurationSeconds() {
        return drivingDurationSeconds;
    }
    public void setDrivingDurationSeconds(int drivingDurationSeconds) {
        this.drivingDurationSeconds = drivingDurationSeconds;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    // Utility method to return the formatted duration in hours and minutes
    public String getFormattedDuration() {
        int hours = drivingDurationSeconds / 3600;
        int minutes = (drivingDurationSeconds % 3600) / 60;
        return String.format("%dh %02dm", hours, minutes);
    }
}
