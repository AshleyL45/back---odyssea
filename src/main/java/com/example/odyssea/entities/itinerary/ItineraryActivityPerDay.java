package com.example.odyssea.entities.itinerary;

import jakarta.validation.constraints.Min;

public class ItineraryActivityPerDay {

    @Min(value = 1, message = "Itinerary Step ID must be greater than or equal to 1")
    private int itineraryStepId;

    @Min(value = 1, message = "Activity ID must be greater than or equal to 1")
    private int activityId;

    @Min(value = 1, message = "Day number must be greater than or equal to 1")
    private int dayNumber;

    public ItineraryActivityPerDay() {}

    public ItineraryActivityPerDay(int itineraryStepId, int activityId, int dayNumber) {
        this.itineraryStepId = itineraryStepId;
        this.activityId = activityId;
        this.dayNumber = dayNumber;
    }

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