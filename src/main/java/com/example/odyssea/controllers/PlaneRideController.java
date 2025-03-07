package com.example.odyssea.controllers;

import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import com.example.odyssea.services.PlaneRideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/planeRide")
public class PlaneRideController {
    private final PlaneRideService planeRideService;

    public PlaneRideController(PlaneRideService planeRideService) {
        this.planeRideService = planeRideService;
    }

    @GetMapping("flight")
    public Mono<List<FlightOffersDTO>> getFlightOffers(@RequestParam String departureIata, @RequestParam String arrivalIata, @RequestParam String departureDate,
                                                       @RequestParam String arrivalDate, @RequestParam int numberOfPeople){
        LocalDate departureDateParsed = LocalDate.parse(departureDate);
        LocalDate arrivalDateParsed = LocalDate.parse(arrivalDate);
        return planeRideService.getAllFlightsFromAmadeus(departureIata, arrivalIata, departureDateParsed, arrivalDateParsed, numberOfPeople);

    }
}
