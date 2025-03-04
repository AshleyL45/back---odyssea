package com.example.odyssea.dtos.UserItinerary;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class UserItineraryDTO {

    private int userId;
    private LocalDate departureDate;
    private String departureCityIata;
    private LocalDate arrivalDate;
    private int duration;
    private BigDecimal startingPrice;
    private List<UserItineraryDayDTO> itineraryDays;


    public UserItineraryDTO() {
    }


    public UserItineraryDTO(int userId, LocalDate departureDate, String departureCityIata, LocalDate arrivalDate, int duration, BigDecimal startingPrice, List<UserItineraryDayDTO> itineraryDays) {
        this.userId = userId;
        this.departureDate = departureDate;
        this.departureCityIata = departureCityIata;
        this.arrivalDate = arrivalDate;
        this.duration = duration;
        this.startingPrice = startingPrice;
        this.itineraryDays = itineraryDays;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureCityIata() {
        return departureCityIata;
    }

    public void setDepartureCityIata(String departureCityIata) {
        this.departureCityIata = departureCityIata;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public List<UserItineraryDayDTO> getItineraryDays() {
        return itineraryDays;
    }

    public void setItineraryDays(List<UserItineraryDayDTO> itineraryDays) {
        this.itineraryDays = itineraryDays;
    }
}
