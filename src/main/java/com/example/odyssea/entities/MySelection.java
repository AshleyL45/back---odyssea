package com.example.odyssea.entities;

import jakarta.validation.constraints.NotNull;

public class MySelection {

    @NotNull
    private int userId;
    @NotNull
    private int itineraryId;

    public MySelection(){}
    public MySelection(int userId, int itineraryId){
        this.userId = userId;
        this.itineraryId = itineraryId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }
}
