package com.example.odyssea.entities;

public class CityDistance {

    private int id;
    private int fromCityId;
    private int toCityId;
    private int drivingDurationSeconds; // Correspond à `drivingDurationSeconds` dans SQL
    private double distanceKm; // Correspond à `distanceKm` dans SQL

    public CityDistance() {}

    /**
     * 🔹 Constructeur principal
     */
    public CityDistance(int id, int fromCityId, int toCityId, int drivingDurationSeconds, double distanceKm) {
        this.id = id;
        this.fromCityId = fromCityId;
        this.toCityId = toCityId;
        this.drivingDurationSeconds = drivingDurationSeconds;
        this.distanceKm = distanceKm;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getFromCityId() { return fromCityId; }

    public void setFromCityId(int fromCityId) { this.fromCityId = fromCityId; }

    public int getToCityId() { return toCityId; }

    public void setToCityId(int toCityId) { this.toCityId = toCityId; }

    public int getDrivingDurationSeconds() { return drivingDurationSeconds; }

    public void setDrivingDurationSeconds(int drivingDurationSeconds) { this.drivingDurationSeconds = drivingDurationSeconds; }

    public double getDistanceKm() { return distanceKm; }

    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    /**
     * 🔹 Retourne la durée formatée "X heures Y minutes"
     */
    public String getFormattedDuration() {
        int hours = drivingDurationSeconds / 3600;
        int minutes = (drivingDurationSeconds % 3600) / 60;
        return hours + "h " + minutes + "min";
    }
}
