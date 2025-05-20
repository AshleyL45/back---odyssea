package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.reservation.BookingConfirmation;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.services.mainTables.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Handles all operations related to bookings, including creating, updating, retrieving, and deleting bookings.")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(
            summary = "Fetch all bookings of the current user",
            description = "Returns a list of all bookings made by the currently authenticated user."
    )
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<BookingConfirmation>>> getAllUserReservations() {
        List<BookingConfirmation> bookingConfirmations = reservationService.getAllUserReservations();
        return ResponseEntity.ok(
                ApiResponse.success("Bookings successfully found.", bookingConfirmations, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Fetch a specific booking by its ID",
            description = "Returns detailed information about a booking, identified by its booking ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingConfirmation>> getReservationById(@PathVariable int id) {
        BookingConfirmation booking = reservationService.getBookingById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Booking successfully found.", booking, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Create a new booking",
            description = "Creates a new booking for the authenticated user based on the booking request data."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createReservation() {
        reservationService.createReservation();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Booking successfully created.", HttpStatus.CREATED)
        );
    }

}
