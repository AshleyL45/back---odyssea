package com.example.odyssea.entities.userItinerary;

import org.springframework.data.relational.core.sql.In;

public class UserItineraryStep {
    private Integer id;
    private Integer userId;
    private Integer userItineraryId;
    private Integer hotelId;
    private Integer cityId;
    private Integer dayNumber;
    private boolean offDay;
    private Integer activityId;
    private Integer planeRideId;

    public UserItineraryStep() {
    }

    public UserItineraryStep(Integer id, Integer userId, Integer userItineraryId, Integer hotelId, Integer cityId, Integer dayNumber, boolean offDay, Integer activityId, Integer planeRideId) {
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


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserItineraryId() {
        return userItineraryId;
    }

    public void setUserItineraryId(Integer userItineraryId) {
        this.userItineraryId = userItineraryId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
