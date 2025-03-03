package com.example.odyssea.dtos;

public class CityDistanceInfoDto {
    private double distanceKm;
    private double drivingDurationHours; // durée convertie de secondes en heures

    public CityDistanceInfoDto(double distanceKm, double drivingDurationHours) {
        this.distanceKm = distanceKm;
        this.drivingDurationHours = drivingDurationHours;
    }

    // Getters et Setters
    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getDrivingDurationHours() {
        return drivingDurationHours;
    }

    public void setDrivingDurationHours(double drivingDurationHours) {
        this.drivingDurationHours = drivingDurationHours;
    }
}
