package com.example.odyssea.entities.userItinerary;

public class UserItineraryStep {
    private int userId;
    private int userItineraryId;
    private int hotelId;
    private int cityId;
    private int dayNumber;
    private boolean offDay;
    private int activityId;
    private int planeRideId;

    public UserItineraryStep() {
    }

    public UserItineraryStep(int userId, int userItineraryId, int hotelId, int cityId, int dayNumber, boolean offDay, int activityId, int planeRideId) {
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getPlaneRideId() {
        return planeRideId;
    }

    public void setPlaneRideId(int planeRideId) {
        this.planeRideId = planeRideId;
    }
}
