package com.example.odyssea.dtos;

import com.example.odyssea.entities.mainTables.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class ReservationDto {

    private int idReservation;
    private int idUser;
    private int idItinerary;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;

    private Double totalPrice;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchase;

    private int numberOfAdults;
    private int numberOfKids;

    public ReservationDto() {}

    public ReservationDto(int idReservation, int idUser, int idItinerary, String status, Date departureDate, Date returnDate, Double totalPrice, Date purchase, int numberOfAdults, int numberOfKids) {
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

    public static ReservationDto fromEntity(Reservation reservation) {
        return new ReservationDto(
                reservation.getIdReservation(),
                reservation.getIdUser(),
                reservation.getIdItinerary(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchase(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids()
        );
    }

    public Reservation toEntity() {
        return new Reservation(
                idReservation,
                idUser,
                idItinerary,
                status,
                departureDate,
                returnDate,
                totalPrice,
                purchase,
                numberOfAdults,
                numberOfKids
        );
    }

    // Getters & Setters

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
