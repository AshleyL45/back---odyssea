package com.example.odyssea.controllers.flight;

import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.services.flight.PlaneRideService;
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

    @GetMapping("/all")
    public Mono<List<FlightItineraryDTO>> getFlights(
            @RequestParam String departureIata,
            @RequestParam String arrivalIata,
            @RequestParam String departureDate,
            @RequestParam String returnDate,
            @RequestParam int adults) {
        return planeRideService.getFlights(
                departureIata,
                arrivalIata,
                LocalDate.parse(departureDate),
                LocalDate.parse(returnDate),
                adults);
    }

}
