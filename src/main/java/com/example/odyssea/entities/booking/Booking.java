package com.example.odyssea.entities.booking;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {

    private int id;
    private int booking;

    @NotNull(message = "User booking cannot be null.")
    private int userId;

    @NotNull(message = "Itinerary booking cannot be null.")
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

    @NotNull(message = "Type cannot be null.")
    private String type;


    public Booking() {
    }


    public Booking( int id, int userId, int itineraryId, String status, LocalDate departureDate, LocalDate returnDate, BigDecimal totalPrice, LocalDate purchaseDate, int numberOfAdults, int numberOfKids, String type) {
        this.id = id;
        this.userId = userId;
        this.itineraryId = itineraryId;
        this.status = status;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.type = type;
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

    public int getBooking() {
        return booking;
    }

    public void setBooking(int booking) {
        this.booking = booking;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
