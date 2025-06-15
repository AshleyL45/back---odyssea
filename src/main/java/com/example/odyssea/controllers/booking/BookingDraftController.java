package com.example.odyssea.controllers.booking;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.booking.ItineraryChoice;
import com.example.odyssea.dtos.booking.Step1Request;
import com.example.odyssea.dtos.draft.NumberTravelers;
import com.example.odyssea.dtos.draft.OptionList;
import com.example.odyssea.services.booking.draft.BookingDraftFinalizerService;
import com.example.odyssea.services.booking.draft.BookingDraftStep1Service;
import com.example.odyssea.services.booking.draft.BookingDraftStep2Service;
import com.example.odyssea.services.booking.draft.BookingDraftStep3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@Tag(name = "Booking Draft", description = "Steps for booking flow")
public class BookingDraftController {

    private final BookingDraftStep1Service step1;
    private final BookingDraftStep2Service step2;
    private final BookingDraftStep3Service step3;
    private final BookingDraftFinalizerService finalizer;

    public BookingDraftController(
            BookingDraftStep1Service step1,
            BookingDraftStep2Service step2,
            BookingDraftStep3Service step3,
            BookingDraftFinalizerService finalizer
    ) {
        this.step1 = step1;
        this.step2 = step2;
        this.step3 = step3;
        this.finalizer = finalizer;
    }

    @Operation(summary = "Step 1 - Itinerary, Type and Departure Date",
            description = "Validates itinerary ID, booking type and departure date")
    @PostMapping("/step1")
    public ResponseEntity<ApiResponse<Void>> handleStep1(
            @Valid @RequestBody Step1Request req
    ) {
        step1.execute(
                req.getItineraryId(),
                req.getType(),
                req.getDate()
        );
        return ResponseEntity
                .ok(ApiResponse.success("Step 1 validated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Step 2 - Travelers numbers",
            description = "Validates the number of travelers")
    @PostMapping("/step2")
    public ResponseEntity<ApiResponse<Void>> handleStep2(
            @Valid @RequestBody NumberTravelers req
    ) {
        step2.execute(
                req.getNumberAdults(),
                req.getNumberKids()
        );
        return ResponseEntity
                .ok(ApiResponse.success("Step 2 validated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Step 3 - Options",
            description = "Validates selected booking options")
    @PostMapping("/step3")
    public ResponseEntity<ApiResponse<Void>> handleStep3(
            @Valid @RequestBody OptionList req
    ) {
        step3.execute(req.getOptions());
        return ResponseEntity
                .ok(ApiResponse.success("Step 3 validated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Step 4 – Override final itinerary",
            description = "Replace draft itinerary")
    @PostMapping("/step4")
    public ResponseEntity<ApiResponse<Void>> overrideItinerary(
            @Valid @RequestBody ItineraryChoice choice
    ) {

        step1.updateItineraryOnly(choice.getItineraryId());
        return ResponseEntity
                .ok(ApiResponse.success("Itinerary successfully updated", HttpStatus.OK));
    }

    @Operation(summary = "Step 5 - Finalize booking",
            description = "Turns the draft into a real booking")
    @PostMapping("/finalize")
    public ResponseEntity<ApiResponse<Void>> finalizeBooking() {
        finalizer.finalizeBookingDraft();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking finalized successfully", HttpStatus.CREATED));
    }
}
