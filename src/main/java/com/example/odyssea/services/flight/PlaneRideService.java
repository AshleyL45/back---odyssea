package com.example.odyssea.services.flight;

import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.dtos.Flight.DictionnaryDTO;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import com.example.odyssea.dtos.Flight.FlightDataDTO;
import com.example.odyssea.dtos.Flight.PlaneRideDTO;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
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
                    BigDecimal totalPrice = BigDecimal.ZERO;

                    // Sauvegarde de chaque segment dans la table flightSegment
                    for (FlightSegmentDTO segmentDTO : itinerary.getSegments()) {
                        flightSegmentService.save(segmentDTO);
                        if (offer.getPrice() != null && offer.getPrice().getTotalPrice() != null) {
                            totalPrice = offer.getPrice().getTotalPrice();
                        }
                    }

                    // Créer un DTO minimal pour PlaneRide
                    PlaneRideDTO planeRideDTO = createFlightDTO(itinerary, offer, flightDataDTO.getDictionnary());
                    if (planeRideDTO != null) {
                        // Création de l'entité PlaneRide en utilisant uniquement les colonnes existantes
                        // Ici, on fixe oneWay à false (par défaut) et on utilise totalPrice et currency du DTO.
                        com.example.odyssea.entities.mainTables.PlaneRide planeRideEntity = new com.example.odyssea.entities.mainTables.PlaneRide(
                                0,                // id (sera généré par la BDD)
                                false,            // one_way (fixé à false)
                                planeRideDTO.getTotalPrice(),    // totalPrice
                                planeRideDTO.getCurrency(), // currency
                                null              // created_at (sera géré par la BDD)
                        );
                        planeRideDao.save(planeRideEntity);
                    }

                    return Mono.just(Collections.singletonList(itinerary));
                });
    }

    /**
     * Méthode pour créer un PlaneRideDTO minimal qui ne contient que les informations présentes dans la table planeRide.
     */
    private PlaneRideDTO createFlightDTO(FlightItineraryDTO itinerary,
                                         FlightOffersDTO flightOffer,
                                         DictionnaryDTO dictionaries) {
        if (itinerary == null || itinerary.getSegments() == null || itinerary.getSegments().isEmpty()) {
            return null;
        }

        PlaneRideDTO planeRideDTO = new PlaneRideDTO();
        // Pour le totalPrice et la devise, utilisez ce qui suit :
        planeRideDTO.setTotalPrice(flightOffer != null && flightOffer.getPrice() != null
                ? flightOffer.getPrice().getTotalPrice()
                : BigDecimal.ZERO);
        planeRideDTO.setCurrency(flightOffer != null && flightOffer.getPrice() != null && flightOffer.getPrice().getCurrency() != null
                ? flightOffer.getPrice().getCurrency()
                : "EUR");

        return planeRideDTO;
    }
}
