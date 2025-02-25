package com.example.odyssea.entities;

public class ItineraryFlight {

    private int idReservation;
    private int idFlight;


    public ItineraryFlight(){}
    public ItineraryFlight(int idReservation, int idFlight){
        this.idReservation = idReservation;
        this.idFlight = idFlight;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }
}

