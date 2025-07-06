package com.example.odyssea.entities.userItinerary.drafts;

public class DraftOptions {
    private Integer userItineraryDraftId;
    private Integer optionId;

    public DraftOptions() {
    }

    public DraftOptions(Integer userItineraryDraftId, Integer optionId) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.optionId = optionId;
    }

    public Integer getUserItineraryDraftId() {
        return userItineraryDraftId;
    }

    public void setUserItineraryDraftId(Integer userItineraryDraftId) {
        this.userItineraryDraftId = userItineraryDraftId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }
}
