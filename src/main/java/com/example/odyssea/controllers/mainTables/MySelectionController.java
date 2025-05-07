package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.services.mainTables.MySelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mySelection")
@Tag(
        name = "My selection",
        description = "Handles all operations related to user selection of itineraries (favorites)."
)
public class MySelectionController {

    private final MySelectionService mySelectionService;

    public MySelectionController(MySelectionService mySelectionService) {
        this.mySelectionService = mySelectionService;
    }

    @Operation(
            summary = "Get all selected itineraries by user",
            description = "Returns the list of itineraries the user has marked as favorite."
    )
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Itinerary>>> getAllUserFavorites() {
        List<Itinerary> favorites = mySelectionService.getUserFavorites();
        return ResponseEntity.ok(
                ApiResponse.success("Favorites retrieved successfully.", favorites, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Add an itinerary to user selection",
            description = "Adds an itinerary to the current user's selection using the itinerary ID."
    )
    @PostMapping("/add/{itineraryId}")
    public ResponseEntity<ApiResponse<Void>> createSelection(@PathVariable int itineraryId) {
        mySelectionService.addToSelection(itineraryId);
        return ResponseEntity.ok(
                ApiResponse.success("Itinerary added to selection.", HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Update a selection",
            description = "Updates a selection entry for a specific itinerary."
    )
    @PutMapping("/{itineraryId}")
    public ResponseEntity<ApiResponse<MySelection>> updateSelection(
            @PathVariable int itineraryId,
            @RequestBody MySelection selection
    ) {
        MySelection updated = mySelectionService.updateSelection(itineraryId, selection);
        return ResponseEntity.ok(
                ApiResponse.success("Selection updated successfully.", updated, HttpStatus.OK)
        );
    }

    @Operation(
            summary = "Remove an itinerary from user selection",
            description = "Removes a specific itinerary from the user's selection."
    )
    @DeleteMapping("/remove/{itineraryId}")
    public ResponseEntity<ApiResponse<Void>> deleteSelection(@PathVariable int itineraryId) {
        mySelectionService.deleteFromSelection(itineraryId);
        return ResponseEntity.ok(
                ApiResponse.success("Itinerary removed from selection.", HttpStatus.OK)
        );
    }
}
