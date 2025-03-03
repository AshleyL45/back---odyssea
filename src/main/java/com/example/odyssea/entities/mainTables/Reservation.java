package com.example.odyssea.entities.mainTables;

import java.util.Date;

public class Reservation {

    private int idReservation;
    private int idUser;
    private int idItinerary;
    private String status;
    private Date departureDate;
    private Date returnDate;
    private Double totalPrice;
    private Date purchase;
    private int numberOfAdults;
    private int numberOfKids;




    public Reservation(){}
    public Reservation(int idReservation, int idUser, int idItinerary, String status, Date departureDate, Date returnDate, Double totalPrice, Date purchase, int numberOfAdults, int numberOfKids){
        this.idReservation = idReservation;
        this.idUser = idUser;
        this.idItinerary = idItinerary;
        this.status = status;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.purchase = purchase;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
    }


    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getPurchase() {
        return purchase;
    }

    public void setPurchase(Date purchase) {
        this.purchase = purchase;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }


    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }


}
