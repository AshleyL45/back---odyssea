package com.example.odyssea.controllers;

import com.example.odyssea.entities.MySelection;
import com.example.odyssea.services.MySelectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mySelection")
public class MySelectionController {

    private final MySelectionService mySelectionService;

    public MySelectionController(MySelectionService mySelectionService) {
        this.mySelectionService = mySelectionService;
    }

    // Récupère toutes les sélections
    @GetMapping
    public List<MySelection> getAllSelections() {
        return mySelectionService.getAllSelections();
    }

    // Récupère les sélections d'un utilisateur donné
    @GetMapping("/user/{userId}")
    public List<MySelection> getSelectionsByUser(@PathVariable int userId) {
        return mySelectionService.getSelectionsByUserId(userId);
    }

    // Récupère les sélections pour un itinéraire donné
    @GetMapping("/itinerary/{itineraryId}")
    public List<MySelection> getSelectionsByItinerary(@PathVariable int itineraryId) {
        return mySelectionService.getSelectionsByItineraryId(itineraryId);
    }

    // Récupère la sélection correspondant à un utilisateur et un itinéraire donnés
    @GetMapping("/user/{userId}/itinerary/{itineraryId}")
    public MySelection getSelection(@PathVariable int userId, @PathVariable int itineraryId) {
        return mySelectionService.getSelection(userId, itineraryId);
    }

    // Crée une nouvelle sélection
    @PostMapping
    public MySelection createSelection(@RequestBody MySelection selection) {
        return mySelectionService.createSelection(selection);
    }

    // Met à jour une sélection pour un utilisateur donné
    @PutMapping("/user/{userId}/itinerary/{itineraryId}")
    public MySelection updateSelection(@PathVariable int userId, @PathVariable int itineraryId, @RequestBody MySelection selection) {
        return mySelectionService.updateSelection(userId, itineraryId, selection);
    }

    // Supprime une sélection correspondant à un utilisateur et un itinéraire donnés
    @DeleteMapping("/user/{userId}/itinerary/{itineraryId}")
    public boolean deleteSelection(@PathVariable int userId, @PathVariable int itineraryId) {
        return mySelectionService.deleteSelection(userId, itineraryId);
    }
}
