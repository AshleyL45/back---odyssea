package com.example.odyssea.entities;

public class MySelection {

    private int idUser;
    private int idItinerary;

    public MySelection(){}
    public MySelection(int idUser, int idItinerary){
        this.idUser = idUser;
        this.idItinerary = idItinerary;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    public int getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(int idItinerary) {
        this.idItinerary = idItinerary;
    }
}
