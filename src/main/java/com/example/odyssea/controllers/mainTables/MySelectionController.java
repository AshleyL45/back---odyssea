package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.services.mainTables.MySelectionService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MySelection>> getAllSelections() {
        return ResponseEntity.ok(mySelectionService.getAllSelections());
    }

    // Récupère les sélections d'un utilisateur sous forme d'itinéraires
    @GetMapping("/{userId}")
    public ResponseEntity<List<Itinerary>> getAllUserFavorites(@PathVariable int userId){
        return ResponseEntity.ok(mySelectionService.getUserFavorites(userId));
    }

    // Récupère les sélections d'un utilisateur donné
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MySelection>> getSelectionsByUser(@PathVariable int userId) {
        return ResponseEntity.ok(mySelectionService.getSelectionsByUserId(userId));
    }

    // Récupère les sélections pour un itinéraire donné
    @GetMapping("/itinerary/{itineraryId}")
    public ResponseEntity<List<MySelection>> getSelectionsByItinerary(@PathVariable int itineraryId) {
        return ResponseEntity.ok(mySelectionService.getSelectionsByItineraryId(itineraryId));
    }

    // Récupère la sélection correspondant à un utilisateur et un itinéraire donnés
    @GetMapping("/user/{userId}/itinerary/{itineraryId}")
    public ResponseEntity<MySelection> getSelection(@PathVariable int userId, @PathVariable int itineraryId) {
        return ResponseEntity.ok(mySelectionService.getSelection(userId, itineraryId));
    }

    // Crée une nouvelle sélection
    @PostMapping("/add")
    public ResponseEntity<MySelection> createSelection(@RequestBody MySelection selection) {
        return ResponseEntity.ok(mySelectionService.createSelection(selection));
    }

    // Met à jour une sélection pour un utilisateur donné
    @PutMapping("/user/{userId}/itinerary/{itineraryId}")
    public ResponseEntity<MySelection> updateSelection(@PathVariable int userId, @PathVariable int itineraryId, @RequestBody MySelection selection) {
        return ResponseEntity.ok(mySelectionService.updateSelection(userId, itineraryId, selection));
    }

    // Supprime une sélection correspondant à un utilisateur et un itinéraire donnés
    @DeleteMapping("/{userId}/remove/{itineraryId}")
    public ResponseEntity<String> deleteSelection(@PathVariable int userId, @PathVariable int itineraryId) {
        boolean isDeleted = mySelectionService.deleteSelection(userId, itineraryId);
       if(isDeleted){
           return ResponseEntity.ok("Selection successfully deleted.");
       } else {
           return ResponseEntity.badRequest().body("Cannot delete reservation of user id " + userId + " with itinerary id " + itineraryId);
       }
    }
}
