package com.example.odyssea.entities.userItinerary.drafts;

public class DraftActivities {
    private Integer userItineraryDraftId;
    private Integer activityId;

    public DraftActivities() {
    }

    public DraftActivities(Integer userItineraryDraftId, Integer activityId) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.activityId = activityId;
    }

    public Integer getUserItineraryDraftId() {
        return userItineraryDraftId;
    }

    public void setUserItineraryDraftId(Integer userItineraryDraftId) {
        this.userItineraryDraftId = userItineraryDraftId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
