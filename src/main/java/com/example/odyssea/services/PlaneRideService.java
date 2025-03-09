package com.example.odyssea.services;

import com.example.odyssea.daos.PlaneRideDao;
import com.example.odyssea.dtos.Flight.FlightDataDTO;
import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import com.example.odyssea.entities.mainTables.PlaneRide;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaneRideService {
    private final PlaneRideDao planeRideDao;
    private final TokenService tokenService;
    private final WebClient webClient;

    public PlaneRideService(PlaneRideDao planeRideDao, TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.planeRideDao = planeRideDao;
        this.tokenService = tokenService;
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com/").build();
    }

    private PlaneRide flightOfferToPlaneRide(FlightOffersDTO flightOffersDTO){
        return new PlaneRide(
                flightOffersDTO.isOneWay(),
                flightOffersDTO.getPrice().getTotalPrice(),
                "EUR",
                LocalDateTime.now()
        );
    }

    // Avoir le vol le plus court
//    public FlightOffersDTO getShortestFlight(List<FlightOffersDTO> flightOffersDTOs){
//        List<String> duration = new ArrayList<>();
//        int size = 0;
//
//        calculateTotalSegmentDuration()
//    }

    // Requête GET avec l'API Amadeus
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
                .map(this::convertToFlightOffersDTO)
                .flatMap( flightOffersDTOS -> {
                    List<PlaneRide> planeRides = flightOffersDTOS.stream() // Convertit chaque offre en Plane Ride et l'ajoute dans une liste
                            .map(this::flightOfferToPlaneRide)
                            .toList();
                    planeRideDao.saveAll(planeRides); // Enregistre la liste dans la BDD
                            return Mono.just(flightOffersDTOS); // Retourne un FLightOfferDTO au controller
                });
    }


    private List<FlightOffersDTO> convertToFlightOffersDTO(FlightDataDTO flightDataDTO){
        List<FlightOffersDTO> flightOffers = new ArrayList<>();
       for (FlightOffersDTO flightOffer : flightDataDTO.getFlightOffers()){
           flightOffers.add(flightOffer);
       }
        return flightOffers;
    }

    private Duration calculateTotalSegmentDuration(List<String> durations){
        Duration totalDuration = Duration.ZERO;
        for(String segmentDuration : durations){
            Duration duration = Duration.parse(segmentDuration);
            totalDuration = totalDuration.plus(duration);
        }

        long hours = totalDuration.toHours();
        long minutes = totalDuration.toMinutes() % 60;

        return totalDuration;

    }

}
