package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.controllers.SuccessResponse;
import com.example.odyssea.dtos.reservation.BookingConfirmation;
import com.example.odyssea.dtos.reservation.BookingRequest;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.services.mainTables.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            summary = "Fetch all bookings of all users",
            description = "Returns a list of all bookings for all users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Bookings successfully found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class)
                            )
                    )
            }
    )

    @GetMapping
    public ResponseEntity<SuccessResponse> getAllReservations() {
        List<BookingConfirmation> bookingConfirmations = reservationService.getAllBookings();
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Bookings successfully found.", bookingConfirmations));
    }

    @Operation(
            summary = "Fetch all bookings of the current user",
            description = "Returns a list of all bookings made by the current user."
    )
    @GetMapping("/")
    public ResponseEntity<SuccessResponse> getAllUserReservations(){
        List<BookingConfirmation> bookingConfirmations = reservationService.getAllUserReservations();
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Bookings successfully found.", bookingConfirmations));
    }

    @Operation(
            summary = "Fetch a specific booking by its ID",
            description = "Returns details of a specific booking identified by its booking ID."
    )
    @GetMapping("/{bookingId}")
    public ResponseEntity<SuccessResponse> getReservationById(@PathVariable int bookingId) {
        BookingConfirmation booking = reservationService.getBookingById(bookingId);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Booking successfully found.", booking));
    }

    @Operation(
            summary = "Create a new booking",
            description = "Creates a new booking based on the provided booking request."
    )
    @PostMapping
    public ResponseEntity<SuccessResponse> createReservation() {
        reservationService.createReservation();
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.CREATED, "Booking successfully created."));
    }

    @Operation(
            summary = "Update the status of a booking",
            description = "Updates the status of an existing booking identified by the booking ID."
    )
    @PatchMapping("/{bookingId}")
    public ResponseEntity<SuccessResponse> updateBookingStatus(@PathVariable int bookingId, @RequestBody String status){
        reservationService.updateReservationStatus(bookingId, status);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.NO_CONTENT, "Booking status successfully updated."));
    }

    @Operation(
            summary = "Update a specific booking",
            description = "Updates the details of a specific booking identified by its booking ID."
    )
    @PutMapping("/{bookingId}")
    public ResponseEntity<SuccessResponse> updateBooking(@PathVariable int bookingId, @Valid @RequestBody Reservation reservation) {
        Reservation reservationUpdated = reservationService.updateReservation(bookingId, reservation);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, "Booking successfully updated", reservationUpdated));
    }

    @Operation(
            summary = "Delete a specific booking",
            description = "Deletes a specific booking identified by its booking ID."
    )
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<SuccessResponse> deleteBooking(@PathVariable int bookingId) {
        reservationService.deleteReservation(bookingId);
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.NO_CONTENT, "Booking successfully deleted."));
    }
}
