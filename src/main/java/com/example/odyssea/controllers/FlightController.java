package com.example.odyssea.controllers;

import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.services.FlightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Mono<List<FlightItineraryDTO>> getFlights(
            @RequestParam String departureIata,
            @RequestParam String arrivalIata,
            @RequestParam String departureDate,
            @RequestParam String returnDate,
            @RequestParam int adults) {
        return flightService.getFlights(
                departureIata,
                arrivalIata,
                LocalDate.parse(departureDate),
                LocalDate.parse(returnDate),
                adults);
    }

}
