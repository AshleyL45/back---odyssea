package com.example.odyssea.entities;

public class ItineraryActivityPerDay {

    private int itineraryStepsId;
    private int activityId;
    private int dayNumber;

    public ItineraryActivityPerDay() {}

    public ItineraryActivityPerDay(int itineraryStepsId, int activityId, int dayNumber) {
        this.itineraryStepsId = itineraryStepsId;
        this.activityId = activityId;
        this.dayNumber = dayNumber;
    }

    public int getItineraryStepsId() {
        return itineraryStepsId;
    }

    public void setItineraryStepsId(int itineraryStepsId) {
        this.itineraryStepsId = itineraryStepsId;
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
