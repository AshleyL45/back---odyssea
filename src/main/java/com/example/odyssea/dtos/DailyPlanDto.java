package com.example.odyssea.dtos;

public class DailyPlanDto {
    private String cityName;
    private String countryName;
    private String hotelName;
    private String hotelDescription;
    private String activityName;
    private String activityDescription;
    private String descriptionPerDay;
    private Integer dayNumber;


    public DailyPlanDto() {
    }

    public DailyPlanDto(String cityName, String countryName, String hotelName, String hotelDescription, String activityName, String activityDescription, String descriptionPerDay, Integer dayNumber) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.descriptionPerDay = descriptionPerDay;
        this.dayNumber = dayNumber;
    }

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

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
}
