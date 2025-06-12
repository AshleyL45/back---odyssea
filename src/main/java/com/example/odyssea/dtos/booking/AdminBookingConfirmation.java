package com.example.odyssea.dtos.booking;

import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AdminBookingConfirmation {
    private Integer bookingId;
    private String userFirstName;
    private String userLastName;
    private BigDecimal price;
    private LocalDate purchaseDate;
    private BookingStatus status;


    public AdminBookingConfirmation() {
    }

    public AdminBookingConfirmation(Integer bookingId, String userFirstName, String userLastName, BigDecimal price, LocalDate purchaseDate, BookingStatus status) {
        this.bookingId = bookingId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.status = status;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public static AdminBookingConfirmation fromEntities(Integer bookingId, User user, BigDecimal price, LocalDate purchaseDate, BookingStatus status){
        return new AdminBookingConfirmation(
                bookingId,
                user.getFirstName(),
                user.getLastName(),
                price,
                purchaseDate,
                status
        );
    }
}
