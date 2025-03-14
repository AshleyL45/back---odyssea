package com.example.odyssea.dtos;

import com.example.odyssea.entities.mainTables.Option;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReservationRecapDTO {
    private int idUser;
    private int idItinerary;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date departureDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date returnDate;
    private BigDecimal totalPrice;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date purchaseDate;
    private int numberOfAdults;
    private int numberOfKids;
    private List<Option> options;
    // private List<> flights; TODO Ajouter les vols

    public ReservationRecapDTO() {

    }


    public ReservationRecapDTO(int idUser, int idItinerary, String status, Date departureDate, Date returnDate, BigDecimal totalPrice, Date purchaseDate, int numberOfAdults, int numberOfKids, List<Option> options) {
        this.idUser = idUser;
        this.idItinerary = idItinerary;
        this.status = status;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.options = options;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
