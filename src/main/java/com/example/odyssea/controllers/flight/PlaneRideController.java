package com.example.odyssea.controllers.flight;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.services.flight.PlaneRideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class PlaneRideController {
    private final PlaneRideService planeRideService;

    public PlaneRideController(PlaneRideService planeRideService) {
        this.planeRideService = planeRideService;
    }

    @Operation(
            summary = "Get available flights",
            description = "Returns a list of available flights between two airports for given dates and number of adults"
    )
    @GetMapping("/all")
    public Mono<ResponseEntity<ApiResponse<List<FlightItineraryDTO>>>> getFlights(
            @RequestParam @Parameter(description = "Departure IATA airport code") String departureIata,
            @RequestParam @Parameter(description = "Arrival IATA airport code") String arrivalIata,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Departure date (YYYY-MM-DD)") String departureDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Return date (YYYY-MM-DD)") String returnDate,
            @RequestParam @Parameter(description = "Number of adult passengers") int adults
    ) {
        return planeRideService
                .getFlights(
                        departureIata,
                        arrivalIata,
                        LocalDate.parse(departureDate),
                        LocalDate.parse(returnDate),
                        adults
                )
                .map(flights -> ResponseEntity.ok(
                        ApiResponse.success("Flights retrieved successfully", flights, HttpStatus.OK)
                ));
    }

}
