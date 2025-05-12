package com.example.odyssea.controllers.itinerary;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.mainTables.ItineraryDetails;
import com.example.odyssea.dtos.mainTables.ItinerarySummary;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.services.itinerary.ItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
@Tag(name = "Itineraries", description = "Endpoints for managing standard travel itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }
    @Operation(summary = "Get all itineraries", description = "Retrieve all itineraries with full entity structure")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Itinerary>>> getAllItineraries() {
        List<Itinerary> itineraries = itineraryService.getAllItineraries();
        return ResponseEntity.ok(ApiResponse.success("All itineraries retrieved successfully", itineraries, HttpStatus.OK));
    }

    @Operation(summary = "Get all itinerary summaries", description = "Retrieve all itineraries with their theme")
    @GetMapping("/themes")
    public ResponseEntity<ApiResponse<List<ItinerarySummary>>> getAllItinerariesSummaries() {
        List<ItinerarySummary> summaries = itineraryService.getAllItinerariesSummaries();
        return ResponseEntity.ok(ApiResponse.success("Itinerary summaries retrieved successfully", summaries, HttpStatus.OK));
    }

    @Operation(summary = "Get itinerary details", description = "Retrieve full details for a given itinerary ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryDetails>> getItineraryDetails(
            @Parameter(description = "Itinerary ID") @PathVariable int id) {
        ItineraryDetails details = itineraryService.getItineraryDetails(id);
        return ResponseEntity.ok(ApiResponse.success("Itinerary details retrieved successfully", details, HttpStatus.OK));
    }

    @Operation(summary = "Search itineraries", description = "Search itineraries using a free-text query")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Itinerary>>> searchItineraries(
            @Parameter(description = "Free-text search query") @RequestParam String query) {
        List<Itinerary> results = itineraryService.searchItineraries(query);
        return ResponseEntity.ok(ApiResponse.success("Itineraries retrieved by search query", results, HttpStatus.OK));
    }


}
