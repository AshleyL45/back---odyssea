package com.example.odyssea.dtos;

import com.example.odyssea.entities.CityDistance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDistanceDto {
    // On attend que l'API renvoie duration en secondes et distance en mètres
    private double duration;
    private double distance;

    private int fromCityId;
    private int toCityId;

    public CityDistanceDto() {}

    // Conversion en entité : on stocke la durée en secondes et on convertit la distance en kilomètres
    public CityDistance toCityDistance() {
        int drivingDurationSeconds = (int) Math.round(duration);
        double distanceKm = Math.round((distance / 1000.0) * 100.0) / 100.0;
        return new CityDistance(0, fromCityId, toCityId, drivingDurationSeconds, distanceKm);
    }

    // Getters et Setters
    public double getDuration() {
        return duration;
    }
    public void setDuration(double duration) {
        this.duration = duration;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
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
}
