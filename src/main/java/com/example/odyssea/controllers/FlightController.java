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

    /*@GetMapping("/shortest")
    public Mono<FlightItineraryDTO> getAFlight(@RequestParam String departureIata, @RequestParam String arrivalIata, @RequestParam String departureDate,
                                               @RequestParam String arrivalDate, @RequestParam int numberOfPeople){
        LocalDate departureDateParsed = LocalDate.parse(departureDate);
        LocalDate arrivalDateParsed = LocalDate.parse(arrivalDate);
        return flightService.getShortestFlightMono(departureIata, arrivalIata, departureDateParsed, arrivalDateParsed, numberOfPeople);
    }*/

    @GetMapping("/all")
    public Mono<List<FlightItineraryDTO>> getAllFlights(@RequestParam String departureIata, @RequestParam String arrivalIata, @RequestParam String departureDate,
                                                  @RequestParam String arrivalDate, @RequestParam int numberOfPeople){
        LocalDate departureDateParsed = LocalDate.parse(departureDate);
        LocalDate arrivalDateParsed = LocalDate.parse(arrivalDate);
        return flightService.getAllFlights(departureIata, arrivalIata, departureDateParsed, arrivalDateParsed, numberOfPeople);
    }
}
