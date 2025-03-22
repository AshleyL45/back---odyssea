package com.example.odyssea.controllers;

import com.example.odyssea.dtos.DailyPlanWithCityDto;
import com.example.odyssea.dtos.ItineraryResponseDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.services.ItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping
    public ResponseEntity<List<Itinerary>> getAllItineraries() {
        List<Itinerary> itineraries = itineraryService.getAllItineraries();
        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Itinerary> getItineraryById(@PathVariable int id) {
        Itinerary itinerary = itineraryService.getItineraryById(id);
        return new ResponseEntity<>(itinerary, HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ItineraryResponseDTO> getItineraryDetails(@PathVariable int id) {
        return ResponseEntity.ok(itineraryService.getItineraryDetails(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Itinerary>> searchItineraries(@RequestParam String query){
        return ResponseEntity.ok(itineraryService.searchItineraries(query));
    }

    @PostMapping
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Itinerary itinerary) {
        Itinerary created = itineraryService.createItinerary(itinerary);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Itinerary> updateItinerary(@PathVariable int id, @RequestBody Itinerary itinerary) {
        Itinerary updated = itineraryService.updateItinerary(id, itinerary);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable int id) {
        boolean deleted = itineraryService.deleteItinerary(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/daily")
    public ResponseEntity<List<DailyPlanWithCityDto>> getDailyPlanWithCity(@PathVariable int id) {
        List<DailyPlanWithCityDto> dailyPlan = itineraryService.getDailyPlanWithCity(id);
        return ResponseEntity.ok(dailyPlan);
    }
}
