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
