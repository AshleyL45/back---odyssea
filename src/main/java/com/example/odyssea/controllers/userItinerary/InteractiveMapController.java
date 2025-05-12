package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.userItinerary.InteractiveMapDto;
import com.example.odyssea.daos.userItinerary.InteractiveMapRepository;
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
            @PathVariable int itineraryId) { //TODO Supprimer le userId et prendre celui de l'utilisateur connecté
        List<InteractiveMapDto> itineraries = interactiveMapRepository.getItineraryForUser(userId, itineraryId);
        return ResponseEntity.ok(itineraries);
    }
}

