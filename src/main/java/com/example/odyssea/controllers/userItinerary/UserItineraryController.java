package com.example.odyssea.controllers.userItinerary;

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
            summary = "Fetch all user itineraries",
            description = "Returns a list of all personalized itineraries for all users."
    )

    @GetMapping("/all")
    public ResponseEntity<List<UserItineraryDTO>> getAllItineraries(){
        List<UserItineraryDTO> userItineraryDTOs = userItineraryService.getAllUserItineraries();
        return ResponseEntity.status(HttpStatus.OK).body(userItineraryDTOs);
    }


    @Operation(
            summary = "Get a user itinerary by ID",
            description = "Returns the details of a specific itinerary based on its unique ID."
    )

    @GetMapping("/{userItineraryId}")
    public ResponseEntity<UserItineraryDTO> getItineraryById(@PathVariable int userItineraryId){
        UserItineraryDTO userItinerary = userItineraryService.getAUserItineraryById(userItineraryId);
        return ResponseEntity.status(HttpStatus.OK).body(userItinerary);
    }


    @Operation(
            summary = "Generate a personalized itinerary",
            description = "Automatically generates a new itinerary based on saved user preferences."
    )

    @GetMapping("/generate")
        public ResponseEntity<UserItineraryDTO> generateItinerary() {
        UserItineraryDTO userItinerary = userItineraryService.generateItinerary();
        return ResponseEntity.status(HttpStatus.CREATED).body(userItinerary);
    }

    @Operation(
            summary = "Update itinerary name",
            description = "Allows the user to rename an existing itinerary using its ID."
    )
    @PatchMapping("/itineraryName/{id}")
    public ResponseEntity<String> updateItineraryName(@PathVariable int id, @RequestBody ItineraryName itineraryName){
        String newItineraryName = itineraryName.getItineraryName();
        userItineraryService.updateItineraryName(id, newItineraryName);
        return ResponseEntity.status(HttpStatus.OK).body("Your personalized trip's name has been successfully updated.");
    }

}
