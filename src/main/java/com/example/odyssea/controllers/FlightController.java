package com.example.odyssea.controllers;

import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.services.FlightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/all")
    public Mono<List<FlightItineraryDTO>> getAFlight(){ // A changer plus tard
        return flightService.getFlights("PAR", "BER", LocalDate.parse("2025-05-01"), LocalDate.parse("2025-05-28"), 2);
    }
}
