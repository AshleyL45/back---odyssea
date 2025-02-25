package com.example.odyssea.entities;

public class ItineraryOption {

    private int idReservation;
    private int idOption;

    public ItineraryOption(){}
    public ItineraryOption(int idReservation, int idOption){
        this.idReservation = idReservation;
        this.idOption = idOption;
    }


    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdOption() {
        return idOption;
    }

    public void setIdOption(int idOption) {
        this.idOption = idOption;
    }
}
