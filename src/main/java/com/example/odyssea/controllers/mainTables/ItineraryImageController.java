package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.services.mainTables.ItineraryImageService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary-images")
@CrossOrigin(origins = "*")
public class ItineraryImageController {

    private final ItineraryImageService service;

    public ItineraryImageController(ItineraryImageService service) {
        this.service = service;
    }

    // GET /api/itinerary-images/{itineraryId}
    @GetMapping("/{itineraryId}")
    public ResponseEntity<List<String>> listImageRoles(@PathVariable int itineraryId) {
        List<String> roles = service.listRoles(itineraryId);
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    // GET /api/itinerary-images/{itineraryId}/{role}
    @GetMapping("/{itineraryId}/{role}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable int itineraryId,
            @PathVariable String role) {

        byte[] data = service.getImageData(itineraryId, role);
        if (data == null || data.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
