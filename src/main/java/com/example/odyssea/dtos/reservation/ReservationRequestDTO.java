package com.example.odyssea.dtos.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReservationRequestDTO {

    @NotNull(message = "User id cannot be null.")
    private int userId;
    @NotNull(message = "Itinerary id cannot be null.")
    private int itineraryId;
    @NotBlank(message = "Status cannot be blank.")
    private String status;
    @NotNull(message = "Departure date cannot be null.")
    private LocalDate departureDate;
    @NotNull(message = "Return date cannot be null.")
    private LocalDate returnDate;
    @Positive(message = "Price cannot be negative.")
    private BigDecimal totalPrice;
    @Positive(message = "Number of adults must be positive.")
    private int numberOfAdults;
    @PositiveOrZero(message = "Number of kids must be positive or equal to zero.")
    private int numberOfKids;
    private List<Integer> optionIds;
    @PositiveOrZero(message = "Flight id cannot be negative.")
    private int planeRideId;

    public ReservationRequestDTO() {
    }

    public ReservationRequestDTO(int userId, int itineraryId, String status, LocalDate departureDate, LocalDate returnDate, BigDecimal totalPrice, int numberOfAdults, int numberOfKids, List<Integer> optionIds, int planeRideId) {
        this.userId = userId;
        this.itineraryId = itineraryId;
        this.status = status;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.optionIds = optionIds;
        this.planeRideId = planeRideId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(Integer numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
    }

    public int getPlaneRideId() {
        return planeRideId;
    }

    public void setPlaneRideId(int planeRideId) {
        this.planeRideId = planeRideId;
    }
}

