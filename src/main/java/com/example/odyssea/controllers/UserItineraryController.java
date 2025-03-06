package com.example.odyssea.controllers;

import com.example.odyssea.dtos.UserItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.UserPreferencesDTO;
import com.example.odyssea.services.UserItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("userItinerary")
public class UserItineraryController {
    private final UserItineraryService userItineraryService;

    public UserItineraryController(UserItineraryService userItineraryService) {
        this.userItineraryService = userItineraryService;
    }

    // Avoir tous les itinéraires personnalisés d'un utilisateur
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<UserItineraryDTO>> getAllUserItineraries(@PathVariable int userId){
        List<UserItineraryDTO> userItineraryDTOs = userItineraryService.getAllUserItineraries(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userItineraryDTOs);
    }

    // Avoir un itinéraire en particulier
    @GetMapping("/{userItineraryId}")
    public ResponseEntity<UserItineraryDTO> getAnItinerary(@PathVariable int userItineraryId){
        UserItineraryDTO userItinerary = userItineraryService.getAUserItineraryById(userItineraryId);
        return ResponseEntity.status(HttpStatus.OK).body(userItinerary);
    }

    // Générer un itinéraire
    @PostMapping("/generate")
        public ResponseEntity<UserItineraryDTO> generateItinerary(@RequestBody UserPreferencesDTO userPreferences){
        UserItineraryDTO userItinerary = userItineraryService.generateUserItinerary(userPreferences);
        return ResponseEntity.status(HttpStatus.CREATED).body(userItinerary);
    }

}
