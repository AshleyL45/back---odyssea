package com.example.odyssea.entities.userItinerary.drafts;

public class DraftCountries {
    private Integer userItineraryDraftId;
    private Integer countryId;
    private Integer position;

    public DraftCountries() {
    }

    public DraftCountries(Integer userItineraryDraftId, Integer countryId, Integer position) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.countryId = countryId;
        this.position = position;
    }


    public Integer getUserItineraryDraftId() {
        return userItineraryDraftId;
    }

    public void setUserItineraryDraftId(Integer userItineraryDraftId) {
        this.userItineraryDraftId = userItineraryDraftId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
