package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.userItinerary.InteractiveMapDto;
import com.example.odyssea.daos.userItinerary.InteractiveMapRepository;
import com.example.odyssea.services.userItinerary.InteractiveMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactive-map")
//@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Interactive Map", description = "Endpoints for the interactive itinerary map")
public class InteractiveMapController {

    private final InteractiveMapService interactiveMapService;

    public InteractiveMapController(InteractiveMapService interactiveMapService) {
        this.interactiveMapService = interactiveMapService;
    }

    @Operation(summary = "Get map data for an itinerary",
            description = "Returns all map segments (cities, paths...) related to the specified itinerary")
    @GetMapping("/{itineraryId}")
    public ResponseEntity<ApiResponse<List<InteractiveMapDto>>> getItineraryForUser(
            @Parameter(description = "Itinerary ID") @PathVariable int itineraryId) {

        List<InteractiveMapDto> segments = interactiveMapService.getItinerariesForUser(itineraryId);

        return ResponseEntity.ok(
                ApiResponse.success("Interactive map data retrieved successfully", segments, HttpStatus.OK)
        );
    }
}

