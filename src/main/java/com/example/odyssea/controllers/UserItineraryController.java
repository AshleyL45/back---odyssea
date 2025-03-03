package com.example.odyssea.controllers;

import com.example.odyssea.dtos.UserItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.UserPreferencesDTO;
import com.example.odyssea.services.UserItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userItinerary")
public class UserItineraryController {
    private final UserItineraryService userItineraryService;

    public UserItineraryController(UserItineraryService userItineraryService) {
        this.userItineraryService = userItineraryService;
    }

    @PostMapping("/generate")
        public ResponseEntity<UserItineraryDTO> generateItinerary(@RequestBody UserPreferencesDTO userPreferences){
        UserItineraryDTO userItinerary = userItineraryService.generateUserItinerary(userPreferences);
        return ResponseEntity.status(HttpStatus.CREATED).body(userItinerary);
    }
}
