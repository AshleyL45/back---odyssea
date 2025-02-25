package com.example.odyssea.entities.userItinerary;

public class UserActivitiesPerDay {
    private int userItineraryStepId;
    private int activityId;
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
