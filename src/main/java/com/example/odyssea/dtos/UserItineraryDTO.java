package com.example.odyssea.dtos;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

public class UserItineraryDTO {
    private int id;
    private int userId;
    private Date startDate;
    private Date endDate;
    private int numberOfPeople;
    private BigDecimal startingPrice;
    private Duration totalDuration;
    private FlightDTO flights;

    public UserItineraryDTO(int id, int userId, Date startDate, Date endDate, int numberOfPeople, BigDecimal startingPrice, Duration totalDuration, FlightDTO flights) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPeople = numberOfPeople;
        this.startingPrice = startingPrice;
        this.totalDuration = totalDuration;
        this.flights = flights;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public FlightDTO getFlights() {
        return flights;
    }

    public void setFlights(FlightDTO flights) {
        this.flights = flights;
    }
}
