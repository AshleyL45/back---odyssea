package com.example.odyssea.entities.userItinerary.drafts;

public class DraftCities {
    private Integer userItineraryDraftId;
    private Integer cityId;

    public DraftCities() {
    }

    public DraftCities(Integer userItineraryDraftId, Integer cityId) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.cityId = cityId;
    }

    public Integer getUserItineraryDraftId() {
        return userItineraryDraftId;
    }

    public void setUserItineraryDraftId(Integer userItineraryDraftId) {
        this.userItineraryDraftId = userItineraryDraftId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
