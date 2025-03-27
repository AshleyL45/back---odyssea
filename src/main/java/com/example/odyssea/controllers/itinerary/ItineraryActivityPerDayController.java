package com.example.odyssea.controllers.itinerary;

import com.example.odyssea.entities.itinerary.ItineraryActivityPerDay;
import com.example.odyssea.services.itinerary.ItineraryActivityPerDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itinerary-activities-per-day")
public class ItineraryActivityPerDayController {

    private final ItineraryActivityPerDayService service;

    @Autowired
    public ItineraryActivityPerDayController(ItineraryActivityPerDayService service) {
        this.service = service;
    }

    // Récupérer toutes les associations
    @GetMapping
    public List<ItineraryActivityPerDay> getAllActivitiesPerDay() {
        return service.getAllActivitiesPerDay();
    }

    // Récupérer une association par clé composite
    @GetMapping("/find")
    public Optional<ItineraryActivityPerDay> getActivityPerDay(@RequestParam int itineraryStepId,
                                                               @RequestParam int activityId,
                                                               @RequestParam int dayNumber) {
        return service.getActivityPerDay(itineraryStepId, activityId, dayNumber);
    }

    // Créer une nouvelle association
    @PostMapping
    public void createActivityPerDay(@RequestBody ItineraryActivityPerDay entity) {
        service.createActivityPerDay(entity);
    }

    // Supprimer une association par clé composite
    @DeleteMapping
    public void deleteActivityPerDay(@RequestParam int itineraryStepId,
                                     @RequestParam int activityId,
                                     @RequestParam int dayNumber) {
        service.deleteActivityPerDay(itineraryStepId, activityId, dayNumber);
    }
}
