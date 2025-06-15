package com.example.odyssea.controllers.booking;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.booking.ItineraryChoice;
import com.example.odyssea.dtos.booking.BookingDate;
import com.example.odyssea.dtos.booking.BookingType;
import com.example.odyssea.dtos.booking.Step1Request;
import com.example.odyssea.services.booking.BookingDraftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.odyssea.dtos.draft.NumberTravelers;
import com.example.odyssea.dtos.draft.OptionList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/book")
@Tag(name = "Booking Draft", description = "Steps for booking flow")
public class BookingDraftController {

    private final BookingDraftService bookingDraftService;

    public BookingDraftController(BookingDraftService bookingDraftService) {
        this.bookingDraftService = bookingDraftService;
    }

    @Operation(summary = "Step 1 - Itinerary, Type and Departure Date", description = "Validates itinerary ID, booking type and departure date")
    @PostMapping("/step1")
    public ResponseEntity<ApiResponse<Void>> handleStep1(
            @Valid @RequestBody Step1Request req) {            // <- un seul @RequestBody
        bookingDraftService.validateItineraryId(req.getItineraryId());
        bookingDraftService.validateType(req.getType());
        bookingDraftService.validateDepartureDate(req.getDate());
        return ResponseEntity.ok(ApiResponse.success("Step 1 validated successfully", HttpStatus.OK));
    }



    @Operation(summary = "Step 2 - Travelers numbers", description = "Validates the number of travelers")
    @PostMapping("/step2")
    public ResponseEntity<ApiResponse<Void>> handleTravelers(@RequestBody NumberTravelers request) {
        bookingDraftService.validateNumberOfAdults(request.getNumberAdults());
        bookingDraftService.validateNumberOfKids(request.getNumberKids());
        return ResponseEntity.ok(ApiResponse.success("Step 2 validated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Step 3 - Options", description = "Validates selected booking options")
    @PostMapping("/step3")
    public ResponseEntity<ApiResponse<Void>> handleOptions(@RequestBody OptionList request) {
        bookingDraftService.validateOptions(request.getOptions());
        return ResponseEntity.ok(ApiResponse.success("Step 3 validated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Step 4 – Override final itinerary", description = "Permet de remplacer l'itineraryId du draft")
    @PostMapping("/step4")
    public ResponseEntity<ApiResponse<Void>> overrideItinerary(
            @Valid @RequestBody ItineraryChoice choice) {
        bookingDraftService.overrideItinerary(choice.getItineraryId());
        return ResponseEntity.ok(ApiResponse.success(
                "Itinerary mis à jour avec succès", HttpStatus.OK));
    }

    @Operation(summary = "Step 5 - Finalize booking", description = "Transforme le draft en véritable réservation")
    @PostMapping("/finalize")
    public ResponseEntity<ApiResponse<Void>> finalizeBooking() {
        bookingDraftService.finalizeBookingDraft();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking finalized successfully", HttpStatus.CREATED));
    }

}



