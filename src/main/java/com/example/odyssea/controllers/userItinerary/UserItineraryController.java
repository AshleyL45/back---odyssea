package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.userItinerary.UserItineraryDTO;
import com.example.odyssea.services.userItinerary.UserItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userItinerary")
@Tag(name = "Personalized trip", description = "Generates and gets a user personalized trips")
public class UserItineraryController {

    private final UserItineraryService userItineraryService;

    public UserItineraryController(UserItineraryService userItineraryService) {
        this.userItineraryService = userItineraryService;
    }

    @Operation(
            summary = "Fetch all personalized itineraries of a user.",
            description = "Returns a list of all personalized itineraries for all users."
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserItineraryDTO>>> getAllItineraries() {
        List<UserItineraryDTO> userItineraryDTOs = userItineraryService.getAllUserItineraries();
        ApiResponse<List<UserItineraryDTO>> response = ApiResponse.success(
                "All personalized itineraries retrieved successfully.",
                userItineraryDTOs,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get a user itinerary by ID",
            description = "Returns the details of a specific itinerary based on its unique ID."
    )
    @GetMapping("/{userItineraryId}")
    public ResponseEntity<ApiResponse<UserItineraryDTO>> getItineraryById(@PathVariable int userItineraryId) {
        UserItineraryDTO userItinerary = userItineraryService.getAUserItineraryById(userItineraryId);
        ApiResponse<UserItineraryDTO> response = ApiResponse.success(
                "Personalized itinerary retrieved successfully.",
                userItinerary,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Generate a personalized itinerary",
            description = "Automatically generates a new personalized itinerary based on user preferences."
    )
    @GetMapping("/generate")
    public ResponseEntity<ApiResponse<UserItineraryDTO>> generateItinerary() {
        UserItineraryDTO userItinerary = userItineraryService.generateItinerary();
        ApiResponse<UserItineraryDTO> response = ApiResponse.success(
                "Personalized itinerary generated successfully.",
                userItinerary,
                HttpStatus.CREATED
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update itinerary name",
            description = "Allows the user to rename an existing personalized itinerary using its ID."
    )
    @PatchMapping("/itineraryName/{id}")
    public ResponseEntity<ApiResponse<Void>> updateItineraryName(@PathVariable int id, @RequestBody ItineraryName itineraryName) {
        userItineraryService.updateItineraryName(id, itineraryName.getItineraryName());
        ApiResponse<Void> response = ApiResponse.success(
                "Itinerary name updated successfully.",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

}
