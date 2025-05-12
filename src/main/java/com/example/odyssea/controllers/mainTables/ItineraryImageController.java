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

    @GetMapping("/{itineraryId}")
    public ResponseEntity<List<String>> listImageRoles(@PathVariable int itineraryId) {
        List<String> roles = service.listRoles(itineraryId);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{itineraryId}/{role}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable int itineraryId,
            @PathVariable String role) {

        byte[] data = service.getImageData(itineraryId, role);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
