package com.example.odyssea.entities.itinerary;

public class ItineraryActivityPerDay {
    private int itineraryStepId;
    private int activityId;
    private int dayNumber;

    public ItineraryActivityPerDay() {}

    public ItineraryActivityPerDay(int itineraryStepId, int activityId, int dayNumber) {
        this.itineraryStepId = itineraryStepId;
        this.activityId = activityId;
        this.dayNumber = dayNumber;
    }

    // Getters et Setters
    public int getItineraryStepId() {
        return itineraryStepId;
    }
    public void setItineraryStepId(int itineraryStepId) {
        this.itineraryStepId = itineraryStepId;
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


