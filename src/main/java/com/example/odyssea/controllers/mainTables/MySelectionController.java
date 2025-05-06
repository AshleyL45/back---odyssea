package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.exceptions.SuccessResponse;
import com.example.odyssea.services.mainTables.MySelectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/")
    public ResponseEntity<List<Itinerary>> getAllUserFavorites(){
        return ResponseEntity.ok(mySelectionService.getUserFavorites());
    }

    // Récupère les sélections d'un utilisateur donné
    @GetMapping("/user")
    public ResponseEntity<List<MySelection>> getSelectionsByUser() {
        return ResponseEntity.ok(mySelectionService.getSelectionsByUserId());
    }

    // Récupère les sélections pour un itinéraire donné
    @GetMapping("/itinerary/{itineraryId}")
    public ResponseEntity<List<MySelection>> getSelectionsByItinerary(@PathVariable int itineraryId) {
        return ResponseEntity.ok(mySelectionService.getSelectionsByItineraryId(itineraryId));
    }

    // Crée une nouvelle sélection
    @PostMapping("/add/{itineraryId}")
    public ResponseEntity<SuccessResponse> createSelection(@RequestBody Map<String, Integer> itinerary) {
        Integer itineraryId = itinerary.get("itineraryId");
        mySelectionService.addToSelection(itineraryId);
        return ResponseEntity.ok(new SuccessResponse(true));
    }

    // Met à jour une sélection pour un utilisateur donné
    @PutMapping("/{itineraryId}")
    public ResponseEntity<MySelection> updateSelection(@PathVariable int itineraryId, @RequestBody MySelection selection) {
        return ResponseEntity.ok(mySelectionService.updateSelection(itineraryId, selection));
    }

    // Supprime une sélection correspondant à un utilisateur et un itinéraire donnés
    @DeleteMapping("/remove/{itineraryId}")
    public ResponseEntity<SuccessResponse> deleteSelection(@PathVariable  Map<String, Integer> itinerary) {
        Integer itineraryId = itinerary.get("itineraryId");
        mySelectionService.deleteFromSelection(itineraryId);
        return ResponseEntity.ok(new SuccessResponse(true));
    }
}
