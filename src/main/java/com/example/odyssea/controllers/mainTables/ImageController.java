package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.services.mainTables.ImageService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{itineraryId}/images")
    public ResponseEntity<List<String>> listImageRoles(@PathVariable int itineraryId) {
        List<String> roles = imageService.listImageRoles(itineraryId);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{itineraryId}/images/{role}")
    public ResponseEntity<byte[]> getItineraryImage(
            @PathVariable int itineraryId,
            @PathVariable String role) {

        byte[] data = imageService.getImageData(itineraryId, role);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
