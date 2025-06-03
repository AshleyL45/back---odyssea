package com.example.odyssea.dtos.reservation;

import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.mainTables.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingConfirmation {

    private int id;
    private Itinerary itinerary;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private String status;
    private int numberOfAdults;
    private int numberOfKids;
    private List<Option> optionList;
    private LocalDate purchaseDate;
    private BigDecimal price;


    public BookingConfirmation() {}

    public BookingConfirmation(int id, Itinerary itinerary, LocalDate departureDate, LocalDate returnDate,
                               String status, int numberOfAdults, int numberOfKids,
                               List<Option> optionList, LocalDate purchaseDate, BigDecimal price) {
        this.id = id;
        this.itinerary = itinerary;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.status = status;
        this.numberOfAdults = numberOfAdults;
        this.numberOfKids = numberOfKids;
        this.optionList = optionList;
        this.purchaseDate = purchaseDate;
        this.price = price;
    }

    // Getters et Setters
    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public static BookingConfirmation fromEntities(
            Reservation reservation,
            Itinerary itinerary,
            List<Option> options
    ) {

        return new BookingConfirmation(
                reservation.getReservationId(),
                itinerary,
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getStatus(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                options,
                reservation.getPurchaseDate(),
                reservation.getTotalPrice()
        );
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
