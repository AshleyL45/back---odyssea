package com.example.odyssea.services;

import com.example.odyssea.daos.PlaneRideDao;
import com.example.odyssea.dtos.Flight.FlightDataDTO;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaneRideService {
    private final PlaneRideDao planeRideDao;
    private final TokenService tokenService;
    private final WebClient webClient;

    public PlaneRideService(PlaneRideDao planeRideDao, TokenService tokenService, WebClient webClient) {
        this.planeRideDao = planeRideDao;
        this.tokenService = tokenService;
        this.webClient = webClient;
    }

    /*private LocalDateTime calculateTotalSegmentDuration(List<String> durations){

    }*/

    // Requête GET avec lAPI Amadeus
    public Mono<List<FlightOffersDTO>> getAllFlightsFromAmadeus(String departureIata, String arrivalIata, LocalDate departureDate, LocalDate arrivalDate, int totalPeople){
        return tokenService.getValidToken()
                .flatMap(token -> (
                        webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("v2/shopping/flight-offers")
                                        .queryParam("originLocationCode", departureIata)
                                        .queryParam("destinationLocationCode", arrivalIata)
                                        .queryParam("departureDate", departureDate.toString())
                                        .queryParam("returnDate", arrivalDate.toString())
                                        .queryParam("adults", totalPeople)
                                        .queryParam("max", 2)
                                        .build()
                                )
                                .header("Authorization", "Bearer " + token)
                                .retrieve()
                                .bodyToMono(FlightDataDTO.class)
                ))
                .map(this::convertToFlightDTO);

    }


    private List<FlightOffersDTO> convertToFlightDTO(FlightDataDTO flightDataDTO){
        return new ArrayList<>(flightDataDTO.getData());
    }

}
