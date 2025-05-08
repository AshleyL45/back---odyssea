package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.mainTables.ItineraryImageDto;
import com.example.odyssea.services.mainTables.ItineraryImageService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary-images")
public class ItineraryImageController {

    private final ItineraryImageService service;

    public ItineraryImageController(ItineraryImageService service) {
        this.service = service;
    }

    @GetMapping("/{itineraryId}")
    public ResponseEntity<List<String>> listImageRoles(@PathVariable int itineraryId) {
        List<String> roles = service.listRoles(itineraryId);
        if (roles == null || roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

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
        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    @GetMapping("/{itineraryId}/dtos")
    public ResponseEntity<List<ItineraryImageDto>> getImageDtos(@PathVariable int itineraryId) {
        List<ItineraryImageDto> dtos = service.getImagesForItinerary(itineraryId);
        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtos);
    }
}
