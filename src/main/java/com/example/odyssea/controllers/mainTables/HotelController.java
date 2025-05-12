package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.services.mainTables.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotels", description = "Endpoints for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(summary = "Get all hotels", description = "Retrieve all hotels from the database")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Hotel>>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(ApiResponse.success("All hotels retrieved successfully", hotels, HttpStatus.OK));
    }

    @Operation(summary = "Get a hotel by ID", description = "Retrieve a hotel by its unique identifier")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Hotel>>> getHotelById(
            @Parameter(description = "Hotel ID") @PathVariable int id) {
        return hotelService.getHotel(id)
                .map(hotel -> ResponseEntity.ok(ApiResponse.success("Hotel retrieved successfully", hotel, HttpStatus.OK)));
    }

    @Operation(summary = "Create a new hotel", description = "Adds a new hotel to the database")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createHotel(@RequestBody HotelDto hotelDto) {
        hotelService.createHotel(hotelDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hotel created successfully", HttpStatus.CREATED));
    }

    @Operation(summary = "Update an existing hotel", description = "Updates a hotel's information")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateHotel(
            @Parameter(description = "Hotel ID") @PathVariable int id,
            @RequestBody HotelDto hotelDto) {
        hotelService.updateHotel(id, hotelDto);
        return ResponseEntity.ok(ApiResponse.success("Hotel updated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Delete a hotel", description = "Deletes a hotel by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHotel(
            @Parameter(description = "Hotel ID") @PathVariable int id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(ApiResponse.success("Hotel deleted successfully", HttpStatus.OK));
    }

    @Operation(summary = "Get hotels by city and star rating", description = "Retrieve hotels for a specific city and star rating")
    @GetMapping("/by-city-and-star")
    public ResponseEntity<ApiResponse<List<Hotel>>> getHotelsByCityAndStar(
            @Parameter(description = "City ID") @RequestParam int cityId,
            @Parameter(description = "Star rating") @RequestParam int starRating) {
        List<Hotel> hotels = hotelService.getHotelsByCityAndStarRating(cityId, starRating);
        return ResponseEntity.ok(ApiResponse.success("Hotels retrieved successfully", hotels, HttpStatus.OK));
    }

    @Operation(summary = "Fetch or create hotel from Amadeus", description = "Find or create a hotel by IATA code and star rating using Amadeus API")
    @GetMapping("/from-amadeus/by-iata-and-save")
    public Mono<ResponseEntity<ApiResponse<HotelDto>>> fetchAndSaveHotel(
            @Parameter(description = "IATA city code") @RequestParam String iataCityCode,
            @Parameter(description = "City ID") @RequestParam int cityId,
            @Parameter(description = "Star rating") @RequestParam int starRating) {
        return hotelService.fetchAndSaveHotelWithStarFromAmadeusByCity(iataCityCode, cityId, starRating)
                .map(hotelDto -> ResponseEntity.ok(ApiResponse.success("Hotel retrieved or created via Amadeus", hotelDto, HttpStatus.OK)));
    }

}
