// src/main/java/com/example/odyssea/controllers/booking/BookingOptionController.java
package com.example.odyssea.controllers.booking;

import com.example.odyssea.dtos.booking.BookingOptionRequest;
import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.services.booking.BookingOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookingOptions")
@Tag(name = "Booking Options", description = "Manage options attached to bookings")
public class BookingOptionController {

    private final BookingOptionService bookingOptionService;

    public BookingOptionController(BookingOptionService bookingOptionService) {
        this.bookingOptionService = bookingOptionService;
    }

    @Operation(
            summary = "Add an option to a booking",
            description = "Associe une option donnée à l'itinéraire en cours de réservation pour l'utilisateur courant"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addOptionToBooking(
            @RequestBody BookingOptionRequest request
    ) {
        bookingOptionService.addOptionToBooking(
                request.getItineraryId(),
                request.getOptionId()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Option ajoutée à la réservation", HttpStatus.CREATED));
    }
}
