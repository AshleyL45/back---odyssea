package com.example.odyssea.controllers;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.BookingStatusUpdate;
import com.example.odyssea.dtos.PriceChange;
import com.example.odyssea.dtos.reservation.AdminBookingConfirmation;
import com.example.odyssea.dtos.reservation.AdminBookingConfirmationDetails;
import com.example.odyssea.dtos.reservation.AdminUserItineraryDetails;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.services.AdminBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "Admin actions for managing standard reservations and personalized trips.")
public class AdminBookingController {
    private final AdminBookingService adminBookingService;

    public AdminBookingController(AdminBookingService adminBookingService) {
        this.adminBookingService = adminBookingService;
    }


    @Operation(
            summary = "Fetch all bookings of all users with filters",
            description = "Returns a list of all bookings. Can filter by status, dates, user search. Requires admin role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<AdminBookingConfirmation>>> getAllReservationsWithFiltersAndSorting(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection
    ) {
        List<AdminBookingConfirmation> bookingConfirmations = adminBookingService.getAllBookingsAndFilter(
                status, search, sortField, sortDirection
        );

        return ResponseEntity.ok(
                ApiResponse.success("Bookings successfully found.", bookingConfirmations, HttpStatus.OK)
        );
    }


    @Operation(
            summary = "Fetch all bookings of all users with filters",
            description = "Returns a list of all bookings. Can filter by status, dates, user search. Requires admin role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userItineraries")
    public ResponseEntity<ApiResponse<List<AdminBookingConfirmation>>> getAllUserItinerariesWithFiltersAndSorting(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection
    ) {
        List<AdminBookingConfirmation> bookingConfirmations = adminBookingService.getAllUserItinerariesAndFilter(
                status, search, sortField, sortDirection
        );

        return ResponseEntity.ok(
                ApiResponse.success("Personalized trips successfully found.", bookingConfirmations, HttpStatus.OK)
        );
    }



    @Operation(
            summary = "Fetch a specific booking by its ID",
            description = "Returns detailed information about a booking, identified by its booking ID. Requires admin privileges."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<AdminBookingConfirmationDetails>> getReservationById(@PathVariable int id) {
        AdminBookingConfirmationDetails booking = adminBookingService.getBookingByIdForAdmin(id);
        return ResponseEntity.ok(
                ApiResponse.success("Booking successfully found.", booking, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Fetch a specific personalized trip by its ID",
            description = "Returns detailed information about a personalized trip, identified by its ID. Requires admin privileges."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userItineraries/{id}")
    public ResponseEntity<ApiResponse<AdminUserItineraryDetails>> getUserItineraryById(@PathVariable int id) {
        AdminUserItineraryDetails itineraryDetails = adminBookingService.getByUserItineraryId(id);
        return ResponseEntity.ok(
                ApiResponse.success("Personalized trip successfully found.", itineraryDetails, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Update the total price of a standard reservation",
            description = "Allows an admin to modify the totalPrice of a reservation"
    )
    @PatchMapping("/bookings/{id}/price")
    public ResponseEntity<ApiResponse<Void>> updateBookingPrice(
            @Parameter(description = "Booking ID of the reservation to update", required = true)
            @PathVariable int id,

            @Parameter(description = "New total price", required = true)
            @RequestBody @Valid PriceChange price
            ) {
        adminBookingService.updateReservationPrice(id, price.getNewPrice());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Booking price successfully updated.", HttpStatus.OK)
        );
    }


    @Operation(
            summary = "Update the status of a booking",
            description = "Updates the status (e.g. confirmed, cancelled) of an existing booking identified by its ID. Requires admin privileges."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateBookingStatus(@PathVariable int id, @RequestBody @Valid  BookingStatusUpdate status) {
        adminBookingService.updateReservationStatus(id, status.getNewStatus());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Booking status successfully updated.", HttpStatus.OK)
        );
    }



    @Operation(
            summary = "Update the booking status of a personalized trip",
            description = "Allows an admin to update the booking status of a user itinerary (e.g. CONFIRMED, CANCELLED, PENDING)"
    )
    @PatchMapping("/userItineraries/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserItineraryStatus(
            @Parameter(description = "ID of the user itinerary", required = true)
            @PathVariable int id,

            @Parameter(description = "New booking status", required = true,
                    schema = @Schema(implementation = BookingStatus.class))
            @RequestBody @Valid BookingStatusUpdate status
            ) {
        adminBookingService.updateUserItineraryStatus(id, status.getNewStatus());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Booking status successfully updated.", HttpStatus.OK)
        );
    }


    @Operation(
            summary = "Update the price of a personalized trip",
            description = "Allows an admin to update the price (startingPrice) of a user itinerary"
    )
    @PatchMapping("/userItineraries/{id}/price")
    public ResponseEntity<ApiResponse<Void>> updateUserItineraryPrice(
            @Parameter(description = "ID of the user itinerary to update", required = true)
            @PathVariable int id,

            @Parameter(description = "New price for the itinerary", required = true)
            @RequestBody @Valid PriceChange price
    ) {
        adminBookingService.updatePrice(id, price.getNewPrice());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Booking price successfully updated.", HttpStatus.OK)
        );
    }


}
