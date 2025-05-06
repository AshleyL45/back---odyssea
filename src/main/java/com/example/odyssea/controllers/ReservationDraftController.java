package com.example.odyssea.controllers;

import com.example.odyssea.dtos.reservation.ItineraryChoice;
import com.example.odyssea.dtos.reservation.ReservationDate;
import com.example.odyssea.dtos.reservation.ReservationType;
import com.example.odyssea.services.mainTables.ReservationDraftService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.odyssea.dtos.draft.NumberTravelers;
import com.example.odyssea.dtos.draft.OptionList;
import com.example.odyssea.exceptions.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/book")
@Tag(name = "Reservation Draft", description = "Steps for booking flow")
public class ReservationDraftController {

    private final ReservationDraftService reservationDraftService;

    public ReservationDraftController(ReservationDraftService reservationDraftService) {
        this.reservationDraftService = reservationDraftService;
    }

    @Operation(summary = "Step 1 - Itinerary, Type and Departure Date", description = "Validates itinerary ID, reservation type and departure date")
    @PostMapping("/step1")
    public ResponseEntity<SuccessResponse> handleItineraryTypeDate(@Valid @RequestBody ItineraryChoice itinerary,
                                                                   @Valid @RequestBody ReservationType type,
                                                                   @RequestBody ReservationDate date) {
        reservationDraftService.validateItineraryId(itinerary.getItineraryId());
        reservationDraftService.validateType(type.getType());
        reservationDraftService.validateDepartureDate(date.getDate());
        return ResponseEntity.ok(new SuccessResponse(true));
    }

    @Operation(summary = "Step 2 - Travelers numbers", description = "Validates the number of travelers")
    @PostMapping("/step2")
    public ResponseEntity<SuccessResponse> handleTravelers(@RequestBody NumberTravelers request) {
        reservationDraftService.validateNumberOfAdults(request.getNumberAdults());
        reservationDraftService.validateNumberOfKids(request.getNumberKids());
        return ResponseEntity.ok(new SuccessResponse(true));
    }

    @Operation(summary = "Step 3 - Options", description = "Validates selected reservation options")
    @PostMapping("/step3")
    public ResponseEntity<SuccessResponse> handleOptions(@RequestBody OptionList request) {
        reservationDraftService.validateOptions(request.getOptions());
        return ResponseEntity.ok(new SuccessResponse(true));
    }
}

