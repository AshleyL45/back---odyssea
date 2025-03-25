package com.example.odyssea.controllers;

import com.example.odyssea.dtos.UserItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.UserRequestDTO;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.services.UserItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("userItinerary")
public class UserItineraryController {
    private final UserItineraryService userItineraryService;

    public UserItineraryController(UserItineraryService userItineraryService) {
        this.userItineraryService = userItineraryService;
    }

    // Avoir tous les itinéraires personnalisés d'un utilisateur
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<UserItinerary>> getAllUserItineraries(@PathVariable int userId){
        List<UserItinerary> userItineraryDTOs = userItineraryService.getAllUserItineraries(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userItineraryDTOs);
    }

    // Avoir un itinéraire en particulier
    @GetMapping("/{userItineraryId}")
    public ResponseEntity<UserItineraryDTO> getAnItinerary(@PathVariable int userItineraryId){
        UserItineraryDTO userItinerary = userItineraryService.getAUserItineraryById(userItineraryId);
        return ResponseEntity.status(HttpStatus.OK).body(userItinerary);
    }

    @PostMapping("/itineraryName/{id}")
    public ResponseEntity<String> updateItineraryName(@PathVariable int id, @RequestBody Map<String, String> itineraryName){
        String newItineraryName = itineraryName.get("itineraryName");
        boolean isUpdated = userItineraryService.updateItineraryName(id, newItineraryName);
        if(isUpdated){
            return ResponseEntity.ok("The name of your personalized itinerary was successfully saved.");
        } else {
            return ResponseEntity.badRequest().body("An error occurred. Please try again.");
        }
    }

    // Générer un itinéraire
    @PostMapping("/generate")
        public ResponseEntity<UserItineraryDTO> generateItinerary(@RequestBody UserRequestDTO userPreferences) throws Exception {
        UserItineraryDTO userItinerary = userItineraryService.generateUserItinerary(userPreferences);
        return ResponseEntity.status(HttpStatus.CREATED).body(userItinerary);
    }

}
