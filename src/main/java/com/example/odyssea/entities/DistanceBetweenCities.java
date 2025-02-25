package com.example.odyssea.entities;

public class DistanceBetweenCities {

    private int id;
    private int fromCityId;
    private int toCityId;
    private int drivingDurationInSeconds; // Durée en secondes
    private double distanceInKm; // Distance en km

    public DistanceBetweenCities() {}

    public DistanceBetweenCities(int id, int fromCityId, int toCityId, int drivingDurationInSeconds, double distanceInKm) {
        this.id = id;
        this.fromCityId = fromCityId;
        this.toCityId = toCityId;
        this.drivingDurationInSeconds = drivingDurationInSeconds;
        this.distanceInKm = distanceInKm;
    }

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

    public int getDrivingDurationInSeconds() {
        return drivingDurationInSeconds;
    }

    public void setDrivingDurationInSeconds(int drivingDurationInSeconds) {
        this.drivingDurationInSeconds = drivingDurationInSeconds;
    }

    public double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    /**
     * 🔹 Convertit la réponse de l'API OpenRouteService en objet `DistanceBetweenCities`
     */
    public static DistanceBetweenCities fromApiResponse(int fromCityId, int toCityId, double distanceInMeters, double durationInSeconds) {
        return new DistanceBetweenCities(
                0, // ID auto-incrémenté par la base
                fromCityId,
                toCityId,
                (int) durationInSeconds, // Convertit en `int` pour éviter les erreurs d'arrondi
                distanceInMeters / 1000 // Convertit les mètres en kilomètres
        );
    }

    /**
     * 🔹 Retourne une durée formatée en "X heures Y minutes"
     */
    public String getFormattedDuration() {
        int hours = drivingDurationInSeconds / 3600;
        int minutes = (drivingDurationInSeconds % 3600) / 60;

        if (hours > 0 && minutes > 0) {
            return hours + " heures " + minutes + " minutes";
        } else if (hours > 0) {
            return hours + " heures";
        } else {
            return minutes + " minutes";
        }
    }
}
