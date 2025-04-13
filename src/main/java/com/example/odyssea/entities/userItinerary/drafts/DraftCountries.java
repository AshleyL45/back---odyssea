package com.example.odyssea.entities.userItinerary.drafts;

public class DraftCountries {
    private Integer userItineraryDraftId;
    private Integer countryId;

    public DraftCountries() {
    }

    public DraftCountries(Integer userItineraryDraftId, Integer countryId) {
        this.userItineraryDraftId = userItineraryDraftId;
        this.countryId = countryId;
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
}
