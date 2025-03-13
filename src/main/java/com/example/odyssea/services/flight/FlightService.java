package com.example.odyssea.services.flight;

import com.example.odyssea.daos.flight.FlightDao;
import com.example.odyssea.dtos.Flight.FlightDataDTO;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import com.example.odyssea.dtos.Flight.FlightDTO;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
import com.example.odyssea.dtos.Flight.DictionnaryDTO;
import com.example.odyssea.entities.mainTables.Flight;
import com.example.odyssea.services.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class FlightService {
    private final FlightDao flightDao;
    private final TokenService tokenService;
    private final WebClient webClient;

    public FlightService(FlightDao flightDao, TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.flightDao = flightDao;
        this.tokenService = tokenService;
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com/").build();
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
                    // Vérifier qu'il y a au moins une offre et un itinéraire
                    if (flightDataDTO == null || flightDataDTO.getData() == null || flightDataDTO.getData().isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }
                    FlightOffersDTO offer = flightDataDTO.getData().get(0);
                    if (offer.getItineraries() == null || offer.getItineraries().isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }
                    // On prend uniquement le premier itinéraire pour créer un vol one-way
                    FlightItineraryDTO itinerary = offer.getItineraries().get(0);
                    FlightDTO flightDTO = createFlightDTO(itinerary, offer, flightDataDTO.getDictionnary());
                    if (flightDTO != null) {
                        Flight flightEntity = new Flight(
                                0,
                                flightDTO.getCompanyName(),
                                flightDTO.getDuration(),
                                flightDTO.getDepartureDateTime().toLocalDate(),
                                flightDTO.getDepartureDateTime().toLocalTime(),
                                flightDTO.getDepartureCityIata(),
                                flightDTO.getArrivalDateTime().toLocalDate(),
                                flightDTO.getArrivalDateTime().toLocalTime(),
                                flightDTO.getArrivalCityIata(),
                                flightDTO.getPrice(),
                                flightDTO.getAirplaneName()
                        );
                        flightDao.save(flightEntity);
                    }
                    return Mono.just(Collections.singletonList(itinerary));
                });
    }

    /**
     * Crée un FlightDTO à partir d'un itinéraire, d'une offre et du dictionnaire fourni par l'API
     */
    private FlightDTO createFlightDTO(FlightItineraryDTO itinerary,
                                      FlightOffersDTO flightOffer,
                                      DictionnaryDTO dictionaries) {
        if (itinerary == null || itinerary.getSegments() == null || itinerary.getSegments().isEmpty()) {
            return null;
        }
        // Pour un vol one-way, on utilise uniquement le premier segment
        FlightSegmentDTO segment = itinerary.getSegments().get(0);
        if (segment == null) {
            return null;
        }
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setDepartureCityIata(segment.getDeparture().getIataCode());
        flightDTO.setDepartureDateTime(segment.getDeparture().getDateTime());
        flightDTO.setArrivalCityIata(segment.getArrival().getIataCode());
        flightDTO.setArrivalDateTime(segment.getArrival().getDateTime());
        flightDTO.setDuration(itinerary.getDuration());
        flightDTO.setPrice(flightOffer != null && flightOffer.getPrice() != null
                ? flightOffer.getPrice().getTotalPrice()
                : BigDecimal.ZERO);

        if (dictionaries != null) {
            String carrierCode = segment.getCarrierCode();
            flightDTO.setCompanyName(dictionaries.getCarriers().getOrDefault(carrierCode, carrierCode));

            String aircraftCode = segment.getAircraftCode() != null ? segment.getAircraftCode().getCode() : "";
            flightDTO.setAirplaneName(dictionaries.getAircraft().getOrDefault(aircraftCode, aircraftCode));
        } else {
            flightDTO.setCompanyName("Inconnu");
            flightDTO.setAirplaneName("Modèle inconnu");
        }

        return flightDTO;
    }

    public Mono<FlightDTO> getFlightDTO(int flightId) {
        return Mono.fromCallable(() -> {
            Flight flight = flightDao.findById(flightId);
            // Conversion de Flight en FlightDTO
            return new FlightDTO();
        });
    }
}
