package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Reservation {

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

    @NotNull(message = "Purchase date cannot be null.")
    private LocalDate purchaseDate;

    @Positive(message = "Number of adults must be positive.")
    private int numberOfAdults;

    @PositiveOrZero(message = "Number of kids must be positive or equal to zero.")
    private int numberOfKids;

    private List<Integer> optionId;

    public Reservation() {
    }

    public Reservation(int userId, int itineraryId, String status, LocalDate departureDate, LocalDate returnDate, BigDecimal totalPrice, LocalDate purchaseDate, int numberOfAdults, int numberOfKids, List<Integer> optionId) {
        this.userId = userId;
        this.itineraryId = itineraryId;
        this.status = status;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.optionId = optionId;
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
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(LocalDate purchaseDate) {
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
    public List<Integer> getOptionIds() {
        return optionId;
    }
    public void setOptionIds(List<Integer> optionId) {
        this.optionId = optionId;
    }
}
