package com.example.odyssea.dtos.UserItinerary;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class UserItineraryDTO {

    private int userId;
    private BigDecimal price;
    private LocalDate departureDate;
    private String departureCityIata;
    private LocalDate arrivalDate;
    private String arrivalCityIata;
    private Duration duration;
    private BigDecimal startingPrice;
    private List<UserItineraryDayDTO> itineraryDays;


    public UserItineraryDTO() {
    }


    public UserItineraryDTO(int userId, BigDecimal price, LocalDate departureDate, String departureCityIata, LocalDate arrivalDate, String arrivalCityIata, Duration duration, BigDecimal startingPrice, List<UserItineraryDayDTO> itineraryDays) {
        this.userId = userId;
        this.price = price;
        this.departureDate = departureDate;
        this.departureCityIata = departureCityIata;
        this.arrivalDate = arrivalDate;
        this.arrivalCityIata = arrivalCityIata;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getArrivalCityIata() {
        return arrivalCityIata;
    }

    public void setArrivalCityIata(String arrivalCityIata) {
        this.arrivalCityIata = arrivalCityIata;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
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
