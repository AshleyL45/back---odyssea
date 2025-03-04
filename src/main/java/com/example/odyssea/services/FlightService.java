package com.example.odyssea.services;

import com.example.odyssea.daos.FlightDao;
import com.example.odyssea.dtos.Flight.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
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


    // Récupère les vols de l'API Amadeus
    public Mono<List<FlightItineraryDTO>> getFlights(String departureIata, String arrivalIata, LocalDate departureDate, LocalDate arrivalDate, int totalPeople){
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

    // Récupère les vols et le met dans une liste "flightList"
    private List<FlightItineraryDTO> convertToFlightDTO(FlightDataDTO flightDataDTO) {
        List<FlightItineraryDTO> itineraryList = new ArrayList<>();

        if (flightDataDTO == null || flightDataDTO.getData() == null) {
            return itineraryList;
        }

        for (FlightOffersDTO offer : flightDataDTO.getData()) {
            System.out.println("Offer itineraries: " + offer.getItineraries());

            if (offer.getItineraries().size() < 2) {
                throw new RuntimeException("There isn't a round trip offer.");
            }

            FlightItineraryDTO outboundItinerary = offer.getItineraries().get(0);
            FlightItineraryDTO returnItinerary = offer.getItineraries().get(1);

            if (outboundItinerary.getSegments().isEmpty() || returnItinerary.getSegments().isEmpty()) {
                System.out.println("One of the itineraries has no segments.");
                continue;
            }

            FlightDTO outboundFlight = createFlightDTO(outboundItinerary, offer, flightDataDTO.getDictionnary());
            FlightDTO returnFlight = createFlightDTO(returnItinerary, offer, flightDataDTO.getDictionnary());

            if (outboundFlight == null || returnFlight == null) {
                System.out.println("Outbound or return flight is null");
                continue;
            }

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
            // System.out.println("Itinerary is null");
            return null;
        }

        FlightSegmentDTO outboundFlight = itinerary.getSegments().get(0);
        FlightSegmentDTO returnFlight = itinerary.getSegments().getLast();

        if (outboundFlight == null || returnFlight == null) {
            //System.out.println("Outbound Flight or Return Flight is null");
            return null;
        }

        FlightDTO flight = new FlightDTO();


        flight.setDepartureCityIata(outboundFlight.getDeparture().getIataCode());
        flight.setArrivalCityIata(returnFlight.getArrival().getIataCode());
        flight.setDepartureDateTime(outboundFlight.getDeparture().getDateTime());
        flight.setArrivalDateTime(returnFlight.getArrival().getDateTime());

        flight.setDuration(itinerary.getDuration());

        flight.setPrice(flightOffer.getPrice().getTotalPrice());
        System.out.println(flightOffer.getPrice().getTotalPrice());

        String carrierCode = outboundFlight.getCarrierCode();  // La compagnie pour l'aller
        flight.setCompanyName(dictionaries.getCarriers().getOrDefault(carrierCode, "Inconnu"));
        System.out.println("Dictionnary company : " + dictionaries.getCarriers());

        String aircraftCode = outboundFlight.getAircraftCode().getCode(); // Le modèle de l'avion
        flight.setAirplaneName(dictionaries.getAircraft().getOrDefault(aircraftCode, "Modèle inconnu"));
        System.out.println("Dictionnary airplane : " + dictionaries.getAircraft());

        return flight;
    }
}
