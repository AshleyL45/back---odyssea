package com.example.odyssea.entities.userItinerary.drafts;

public class DraftCities {
    private Integer userItineraryDraftId;
    private Integer cityId;
    private Integer position;

    public DraftCities() {
    }

    public DraftCities(Integer userItineraryDraftId, Integer cityId, Integer position) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.cityId = cityId;
        this.position = position;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
