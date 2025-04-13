package com.example.odyssea.entities.userItinerary.drafts;

import org.springframework.data.relational.core.sql.In;

public class DraftActivities {
    private Integer userItineraryDraftId;
    private Integer activityId;
    private Integer position;

    public DraftActivities() {
    }

    public DraftActivities(Integer userItineraryDraftId, Integer activityId, Integer position) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.activityId = activityId;
        this.position = position;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
