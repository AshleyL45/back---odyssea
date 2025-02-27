package com.example.odyssea.services;

import com.example.odyssea.daos.FlightDao;
import com.example.odyssea.dtos.FlightDTO;
import com.example.odyssea.entities.mainTables.Flight;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
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

    public FlightDTO getFlightDTO(int id){
        Flight flight = flightDao.findById(id);
        return new FlightDTO(flight.getId(), flight.getCompanyName(), flight.getDuration(), flight.getDepartureDate(), flight.getDepartureTime(), flight.getDepartureCityIata(), flight.getArrivalDate(), flight.getArrivalTime(), flight.getArrivalCityIata(), flight.getPrice(), flight.getAirplaneName());
    }

    public Mono<List<FlightDTO>> getFlights(String departureIata, String arrivalIata, LocalDate departureDate, LocalDate arrivalDate, int totalPeople){

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
                                        .bodyToMono(new ParameterizedTypeReference<List<FlightDTO>>() {})
                                ))
                        .flatMap(flights ->
                        // Sauvegarde chaque vol dans la BDD de façon asynchrone
                        Mono.fromRunnable(() -> flights.forEach(flightDao::save))
                                .thenReturn(flights) // Retourne la liste après sauvegarde
        );

    }
}
