package com.example.odyssea.entities.mainTables;

public class City {

    private int id;
    private int countryId;
    private String name;
    private String iataCode;
    private double longitude;
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
