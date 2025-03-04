package com.example.odyssea.services;

import com.example.odyssea.daos.FlightDao;
import com.example.odyssea.dtos.Flight.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

   /*public FlightDTO getFlightDTO(int id){
        Flight flight = flightDao.findById(id);
        return new FlightDTO(flight.getId(), flight.getCompanyName(), flight.getDuration(), flight.getDepartureDate(), flight.getDepartureTime(), flight.getDepartureCityIata(), flight.getArrivalDate(), flight.getArrivalTime(), flight.getArrivalCityIata(), flight.getPrice(), flight.getAirplaneName());
    }*/

    // Renvoie le vol le plus court au controller
    public Mono<FlightItineraryDTO> getShortestFlight(String departureIata, String arrivalIata, LocalDate departureDate, LocalDate arrivalDate, int totalPeople){
        return getAllFlights(departureIata, arrivalIata, departureDate, arrivalDate, totalPeople)
                .map(flightItineraries -> getShortestFlight(flightItineraries));

    }

    // Récupère les vols de l'API Amadeus
    private Mono<List<FlightItineraryDTO>> getAllFlights(String departureIata, String arrivalIata, LocalDate departureDate, LocalDate arrivalDate, int totalPeople){
        return tokenService.getValidToken()
                        .flatMap(token -> (
                                webClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                .path("v2/shopping/flight-offers")
                                                .queryParam("originLocationCode", departureIata) // A changer plus tard
                                                .queryParam("destinationLocationCode", arrivalIata) // A changer plus tard
                                                .queryParam("departureDate", departureDate.toString()) // A changer plus tard
                                                .queryParam("returnDate", arrivalDate.toString()) // A changer plus tard
                                                .queryParam("adults", totalPeople) // A changer plus tard
                                                .queryParam("max", 2) // A changer plus tard
                                                .build()
                                        )
                                        .header("Authorization", "Bearer " + token)
                                        .retrieve()
                                        .bodyToMono(FlightDataDTO.class)
                                ))
                    .map(this::convertToFlightDTO);
                        /*.flatMap(flights ->
                        // Sauvegarde chaque vol dans la BDD de façon asynchrone
                        Mono.fromRunnable(() -> flights.forEach(flightDao::save))
                                .thenReturn(flights) // Retourne la liste après sauvegarde
        );*/

    }

    // Récupère les vols (qui sont sous forme de FlightDataDTO) et le transforme dans une liste "itineraryList"
    private List<FlightItineraryDTO> convertToFlightDTO(FlightDataDTO flightDataDTO) {
        List<FlightItineraryDTO> itineraryList = new ArrayList<>();

        if (flightDataDTO == null || flightDataDTO.getData() == null) {
            return itineraryList;
        }

        for (FlightOffersDTO offer : flightDataDTO.getData()) {

            if (offer.getItineraries().size() < 2) {
                throw new RuntimeException("There isn't a round trip offer.");
            }

            FlightItineraryDTO outboundItinerary = offer.getItineraries().get(0);
            FlightItineraryDTO returnItinerary = offer.getItineraries().get(1);

            if (outboundItinerary.getSegments().isEmpty() || returnItinerary.getSegments().isEmpty()) {
               throw  new RuntimeException("One of the itineraries has no segments (outbound or return flight).");
            }

            FlightDTO outboundFlight = createFlightDTO(outboundItinerary, offer, flightDataDTO.getDictionnary());
            FlightDTO returnFlight = createFlightDTO(returnItinerary, offer, flightDataDTO.getDictionnary());

            FlightItineraryDTO itinerary = new FlightItineraryDTO();
            itinerary.setSegments(outboundItinerary.getSegments());
            itinerary.setDuration(outboundItinerary.getDuration());

            itineraryList.add(itinerary);
        }

        return itineraryList;
    }


    // Crée un Flight DTO
    private FlightDTO createFlightDTO(FlightItineraryDTO itinerary, FlightOffersDTO flightOffer, DictionnaryDTO dictionaries) {

        if (itinerary == null){
            return null;
        }

        FlightSegmentDTO firstSegment = itinerary.getSegments().get(0);
        FlightSegmentDTO lastSegment = itinerary.getSegments().getLast();

        if (firstSegment == null || lastSegment == null) {
            return null;
        }

        FlightDTO flight = new FlightDTO();


        flight.setDepartureCityIata(firstSegment.getDeparture().getIataCode());
        flight.setArrivalCityIata(lastSegment.getArrival().getIataCode());
        flight.setDepartureDateTime(firstSegment.getDeparture().getDateTime());
        flight.setArrivalDateTime(lastSegment.getArrival().getDateTime());

        flight.setDuration(itinerary.getDuration());

        flight.setPrice(flightOffer.getPrice().getTotalPrice());

        String carrierCode = firstSegment.getCarrierCode();  // La compagnie pour l'aller
        flight.setCompanyName(dictionaries.getCarriers().getOrDefault(carrierCode, "Inconnu"));
        //System.out.println("Dictionnary company : " + dictionaries.getCarriers());

        String aircraftCode = firstSegment.getAircraftCode().getCode(); // Le modèle de l'avion
        flight.setAirplaneName(dictionaries.getAircraft().getOrDefault(aircraftCode, "Modèle inconnu"));
        //System.out.println("Dictionnary airplane : " + dictionaries.getAircraft());

        return flight;
    }

    // Trouver le vol le plus court
    private FlightItineraryDTO getShortestFlight(List<FlightItineraryDTO> flightItineraries){
        return flightItineraries
                .stream()
                .min(Comparator.comparing(flightItineraryDTO -> flightItineraryDTO.getDuration().toHours()))
                .orElse(null);

    }
}
