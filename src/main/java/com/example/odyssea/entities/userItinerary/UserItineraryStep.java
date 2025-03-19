package com.example.odyssea.entities.userItinerary;

import org.springframework.data.relational.core.sql.In;

public class UserItineraryStep {
    private int id;
    private int userId;
    private int userItineraryId;
    private int hotelId;
    private int cityId;
    private int dayNumber;
    private boolean offDay;
    private Integer activityId;
    private Integer planeRideId;

    public UserItineraryStep() {
    }

    public UserItineraryStep(int id, int userId, int userItineraryId, int hotelId, int cityId, int dayNumber, boolean offDay, Integer activityId, Integer planeRideId) {
        this.id = id;
        this.userId = userId;
        this.userItineraryId = userItineraryId;
        this.hotelId = hotelId;
        this.cityId = cityId;
        this.dayNumber = dayNumber;
        this.offDay = offDay;
        this.activityId = activityId;
        this.planeRideId = planeRideId;
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getPlaneRideId() {
        return planeRideId;
    }

    public void setPlaneRideId(Integer planeRideId) {
        this.planeRideId = planeRideId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
