package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.mainTables.ItineraryImageDto;
import com.example.odyssea.services.mainTables.ItineraryImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")

public class ItineraryController {

    private final ItineraryImageService service;

    public ItineraryController(ItineraryImageService service) {
        this.service = service;
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<ItineraryImageDto>> getItineraryImages(@PathVariable("id") int id) {
        List<ItineraryImageDto> images = service.getImagesForItinerary(id);
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }
}
