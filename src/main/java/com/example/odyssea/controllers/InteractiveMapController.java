package com.example.odyssea.controllers;

import com.example.odyssea.dtos.InteractiveMapDto;
import com.example.odyssea.dtos.InteractiveMapRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactive-map")
@CrossOrigin(origins = "http://localhost:3000")

public class InteractiveMapController {

    private final InteractiveMapRepository interactiveMapRepository;

    public InteractiveMapController(InteractiveMapRepository interactiveMapRepository) {
        this.interactiveMapRepository = interactiveMapRepository;
    }

    @GetMapping("/user/{userId}/itinerary/{itineraryId}")
    public ResponseEntity<List<InteractiveMapDto>> getItineraryForUser(
            @PathVariable int userId,
            @PathVariable int itineraryId) {
        List<InteractiveMapDto> itineraries = interactiveMapRepository.getItineraryForUser(userId, itineraryId);
        return ResponseEntity.ok(itineraries);
    }
}

