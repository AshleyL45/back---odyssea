package com.example.odyssea.entities.userItinerary;

import jakarta.validation.constraints.Min;

public class UserActivitiesPerDay {

    @Min(value = 1, message = "User Itinerary Step ID must be greater than or equal to 1")
    private int userItineraryStepId;

    @Min(value = 1, message = "Activity ID must be greater than or equal to 1")
    private int activityId;

    @Min(value = 1, message = "Day number must be greater than or equal to 1")
    private int dayNumber;

    public UserActivitiesPerDay(int userItineraryStepId, int activityId, int dayNumber) {
        this.userItineraryStepId = userItineraryStepId;
        this.activityId = activityId;
        this.dayNumber = dayNumber;
    }

    public int getUserItineraryStepId() {
        return userItineraryStepId;
    }

    public void setUserItineraryStepId(int userItineraryStepId) {
        this.userItineraryStepId = userItineraryStepId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}
