package com.example.odyssea.entities.userItinerary;

import jakarta.validation.constraints.Min;

public class UserItineraryStep {

    @Min(value = 1, message = "User ID must be greater than or equal to 1")
    private int userId;

    @Min(value = 1, message = "User Itinerary ID must be greater than or equal to 1")
    private int userItineraryId;

    @Min(value = 1, message = "Hotel ID must be greater than or equal to 1")
    private int hotelId;

    @Min(value = 1, message = "City ID must be greater than or equal to 1")
    private int cityId;

    @Min(value = 1, message = "Day number must be greater than or equal to 1")
    private int dayNumber;

    private boolean offDay;

    @Min(value = 1, message = "Activity ID must be greater than or equal to 1")
    private int activityId;

    @Min(value = 1, message = "Flight ID must be greater than or equal to 1")
    private int flightId;

    public UserItineraryStep() {
    }

    public UserItineraryStep(int userId, int userItineraryId, int hotelId, int cityId, int dayNumber, boolean offDay, int activityId, int flightId) {
        this.userId = userId;
        this.userItineraryId = userItineraryId;
        this.hotelId = hotelId;
        this.cityId = cityId;
        this.dayNumber = dayNumber;
        this.offDay = offDay;
        this.activityId = activityId;
        this.flightId = flightId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserItineraryId() {
        return userItineraryId;
    }
    public void setUserItineraryId(int userItineraryId) {
        this.userItineraryId = userItineraryId;
    }
    public int getHotelId() {
        return hotelId;
    }
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public int getDayNumber() {
        return dayNumber;
    }
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
    public boolean isOffDay() {
        return offDay;
    }
    public void setOffDay(boolean offDay) {
        this.offDay = offDay;
    }
    public int getActivityId() {
        return activityId;
    }
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
    public int getFlightId() {
        return flightId;
    }
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }
}