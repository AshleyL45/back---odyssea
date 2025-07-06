package com.example.odyssea.dtos.mainTables;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyPlanWithCityDto {
    private String cityName;
    private String countryName;
    private String hotelName;
    private String hotelDescription;
    private String activityName;
    private String activityDescription;
    private String descriptionPerDay;
    private int dayNumber;
    private double latitude;
    private double longitude;

    public DailyPlanWithCityDto(String cityName, String countryName, String hotelName, String hotelDescription,
                                String activityName, String activityDescription, String descriptionPerDay,
                                int dayNumber, double latitude, double longitude) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.descriptionPerDay = descriptionPerDay;
        this.dayNumber = dayNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Méthode statique de mapping depuis ResultSet
    public static DailyPlanWithCityDto fromResultSet(ResultSet rs) throws SQLException {
        return new DailyPlanWithCityDto(
                rs.getString("cityName"),
                rs.getString("countryName"),
                rs.getString("hotelName"),
                rs.getString("hotelDescription"),
                rs.getString("activityName"),
                rs.getString("activityDescription"),
                rs.getString("description_per_day"),
                rs.getInt("day_number"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")
        );
    }

    // Getters et setters

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getDescriptionPerDay() {
        return descriptionPerDay;
    }

    public void setDescriptionPerDay(String descriptionPerDay) {
        this.descriptionPerDay = descriptionPerDay;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
