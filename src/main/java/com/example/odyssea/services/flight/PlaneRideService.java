package com.example.odyssea.services.flight;

import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.dtos.Flight.DictionnaryDTO;
import com.example.odyssea.dtos.Flight.FlightDataDTO;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
import com.example.odyssea.dtos.Flight.PlaneRideDTO;
import com.example.odyssea.services.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class PlaneRideService {

    private final PlaneRideDao planeRideDao;
    private final TokenService tokenService;
    private final WebClient webClient;
    private final FlightSegmentService flightSegmentService;

    public PlaneRideService(PlaneRideDao planeRideDao,
                            TokenService tokenService,
                            WebClient.Builder webClientBuilder,
                            FlightSegmentService flightSegmentService) {
        this.planeRideDao = planeRideDao;
        this.tokenService = tokenService;
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com/").build();
        this.flightSegmentService = flightSegmentService;
    }

    public Mono<List<FlightItineraryDTO>> getFlights(String departureIata, String arrivalIata,
                                                     LocalDate departureDate, LocalDate arrivalDate,
                                                     int totalPeople) {
        return tokenService.getValidToken()
                .flatMap(token ->
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
                )
                .flatMap(flightDataDTO -> {
                    if (flightDataDTO == null || flightDataDTO.getData() == null || flightDataDTO.getData().isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }
                    FlightOffersDTO offer = flightDataDTO.getData().get(0);
                    if (offer.getItineraries() == null || offer.getItineraries().isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }
                    FlightItineraryDTO itinerary = offer.getItineraries().get(0);
                    DictionnaryDTO dict = flightDataDTO.getDictionnary();

                    // Enrichir chaque segment avec carrierName et aircraftName à partir du dictionnaire
                    for (FlightSegmentDTO segmentDTO : itinerary.getSegments()) {
                        if (dict != null) {
                            if (dict.getCarriers() != null && dict.getCarriers().containsKey(segmentDTO.getCarrierCode())) {
                                segmentDTO.setCarrierName(dict.getCarriers().get(segmentDTO.getCarrierCode()));
                            }
                            if (dict.getAircraft() != null && segmentDTO.getAircraftCode() != null &&
                                    dict.getAircraft().containsKey(segmentDTO.getAircraftCode().getCode())) {
                                segmentDTO.setAircraftName(dict.getAircraft().get(segmentDTO.getAircraftCode().getCode()));
                            }
                        }
                        // Sauvegarde de chaque segment enrichi
                        flightSegmentService.save(segmentDTO);
                    }

                    // Utilisation du prix global fourni par l'API Amadeus
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    if (offer.getPrice() != null && offer.getPrice().getTotalPrice() != null) {
                        totalPrice = offer.getPrice().getTotalPrice();
                    }
                    // Création d'un DTO minimal pour PlaneRide à partir du prix global
                    PlaneRideDTO planeRideDTO = createFlightDTO(offer);
                    if (planeRideDTO != null) {
                        planeRideDTO.setTotalPrice(totalPrice);
                        // Création de l'entité PlaneRide
                        com.example.odyssea.entities.mainTables.PlaneRide planeRideEntity = new com.example.odyssea.entities.mainTables.PlaneRide(
                                0,
                                true,                      // one_way
                                planeRideDTO.getTotalPrice(),
                                planeRideDTO.getCurrency(),
                                null
                        );
                        planeRideDao.save(planeRideEntity);
                    }
                    return Mono.just(Collections.singletonList(itinerary));
                });
    }

    /**
     * Méthode pour créer un PlaneRideDTO minimal avec les informations nécessaires
     * pour alimenter la table planeRide.
     */
    private PlaneRideDTO createFlightDTO(FlightOffersDTO flightOffer) {
        if (flightOffer == null || flightOffer.getPrice() == null) {
            return null;
        }
        PlaneRideDTO planeRideDTO = new PlaneRideDTO();
        planeRideDTO.setCurrency(flightOffer.getPrice().getCurrency() != null
                ? flightOffer.getPrice().getCurrency() : "EUR");
        planeRideDTO.setTotalPrice(BigDecimal.ZERO);
        return planeRideDTO;
    }
}
