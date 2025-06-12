package com.example.odyssea.dtos.booking;

import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userAuth.User;

import java.math.BigDecimal;
import java.util.List;

public class AdminBookingConfirmationDetails {
    private String userFirstName;
    private String userLastName;
    private String itineraryName;
    private BigDecimal itineraryPrice;
    private Booking booking;
    private List<Option> options;

    public AdminBookingConfirmationDetails() {
    }

    public AdminBookingConfirmationDetails(String userFirstName, String userLastName, String itineraryName, BigDecimal itineraryPrice, Booking booking, List<Option> options) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.itineraryName = itineraryName;
        this.itineraryPrice = itineraryPrice;
        this.booking = booking;
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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public static AdminBookingConfirmationDetails fromEntities(User user, Itinerary itinerary, Booking booking, List<Option> options){
        return new AdminBookingConfirmationDetails(
                user.getFirstName(),
                user.getLastName(),
                itinerary.getName(),
                itinerary.getPrice(),
                booking,
                options
        );
    }
}
