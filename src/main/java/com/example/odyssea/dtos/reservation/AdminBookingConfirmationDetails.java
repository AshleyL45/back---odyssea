package com.example.odyssea.dtos.reservation;

import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.entities.userAuth.User;

import java.math.BigDecimal;
import java.util.List;

public class AdminBookingConfirmationDetails {
    private String userFirstName;
    private String userLastName;
    private String itineraryName;
    private BigDecimal itineraryPrice;
    private Reservation reservation;
    private List<Option> options;

    public AdminBookingConfirmationDetails() {
    }

    public AdminBookingConfirmationDetails(String userFirstName, String userLastName, String itineraryName, BigDecimal itineraryPrice, Reservation reservation, List<Option> options) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.itineraryName = itineraryName;
        this.itineraryPrice = itineraryPrice;
        this.reservation = reservation;
        this.options = options;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public BigDecimal getItineraryPrice() {
        return itineraryPrice;
    }

    public void setItineraryPrice(BigDecimal itineraryPrice) {
        this.itineraryPrice = itineraryPrice;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public static AdminBookingConfirmationDetails fromEntities(User user, Itinerary itinerary, Reservation reservation, List<Option> options){
        return new AdminBookingConfirmationDetails(
                user.getFirstName(),
                user.getLastName(),
                itinerary.getName(),
                itinerary.getPrice(),
                reservation,
                options
        );
    }
}
