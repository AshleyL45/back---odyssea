package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.FlightSegmentDao;
import com.example.odyssea.daos.FlightSegmentRideDao;
import com.example.odyssea.daos.PlaneRideDao;
import com.example.odyssea.dtos.Flight.*;
import com.example.odyssea.dtos.UserItinerary.CitySelectionDTO;
import com.example.odyssea.dtos.UserItinerary.CountrySelectionDTO;
import com.example.odyssea.dtos.UserItinerary.UserPreferencesDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.FlightSegment;
import com.example.odyssea.entities.mainTables.FlightSegmentRide;
import com.example.odyssea.entities.mainTables.PlaneRide;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlaneRideService {
    private final PlaneRideDao planeRideDao;
    private final FlightSegmentDao flightSegmentDao;
    private final FlightSegmentRideDao flightSegmentRideDao;
    private final CityDao cityDao;
    private final TokenService tokenService;
    private final WebClient webClient;

    public PlaneRideService(PlaneRideDao planeRideDao, FlightSegmentDao flightSegmentDao, FlightSegmentRideDao flightSegmentRideDao, CityDao cityDao, TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.planeRideDao = planeRideDao;
        this.flightSegmentDao = flightSegmentDao;
        this.flightSegmentRideDao = flightSegmentRideDao;
        this.cityDao = cityDao;
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

    // Requête GET avec l'API Amadeus
    public Mono<List<FlightOffersDTO>> getAllFlightsFromAmadeus(String departureIata, String arrivalIata, LocalDate departureDate, LocalDate arrivalDate, int totalPeople) {
        return tokenService.getValidToken()
                .flatMap(token -> {
                    System.out.println("Token généré: " + token);
                    return webClient.get()
                            .uri(uriBuilder -> {
                                System.out.println("Requête envoyée à Amadeus: " + uriBuilder.build().toString());
                                return uriBuilder
                                        .path("v2/shopping/flight-offers")
                                        .queryParam("originLocationCode", departureIata)
                                        .queryParam("destinationLocationCode", arrivalIata)
                                        .queryParam("departureDate", departureDate.toString())
                                        .queryParam("adults", totalPeople)
                                        .queryParam("max", 1)
                                        .build();
                            })
                            .header("Authorization", "Bearer " + token)
                            .retrieve()
                            .bodyToMono(FlightDataDTO.class)
                            .doOnError(error -> {
                                System.err.println("Erreur lors de la requête API: " + error.getMessage());
                                if (error instanceof WebClientResponseException) {
                                    WebClientResponseException responseException = (WebClientResponseException) error;
                                    System.err.println("Status code: " + responseException.getStatusCode());
                                    System.err.println("Response body: " + responseException.getResponseBodyAsString());
                                }
                            })
                            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)));
                })
                .map(this::convertToFlightOffersDTO)
                .onErrorReturn(Collections.emptyList());
    }



    public Mono<List<PlaneRide>> generateItineraryFlights(UserPreferencesDTO userPreferences){
        List<String> departureCities = new ArrayList<>();
        List<String> arrivalCities = new ArrayList<>();
        List<LocalDate> flightDates = new ArrayList<>();

        LocalDate startDate = userPreferences.getStartDate();
        int stayDuration = 4; // Chaque pays est visité pendant 4 jours

        List<CountrySelectionDTO> countrySelection = userPreferences.getCountrySelection();

        for (int i = 0; i < countrySelection.size(); i++) {
            List<CitySelectionDTO> citySelection = countrySelection.get(i).getCitySelection();

            // On choisit la première ville du pays comme destination principale
            String mainCity = citySelection.getFirst().getCityName();

            if (departureCities.isEmpty()) {
                departureCities.add(userPreferences.getDepartureCity());
            } else {
                departureCities.add(arrivalCities.getLast());
            }

            arrivalCities.add(mainCity);
            flightDates.add(startDate.plusDays(i * stayDuration));
        }

        // Ajout du vol retour vers la ville de départ
        departureCities.add(arrivalCities.getLast());
        arrivalCities.add(userPreferences.getDepartureCity());
        flightDates.add(startDate.plusDays(countrySelection.size() * stayDuration));

        List<Mono<List<FlightOffersDTO>>> flightRequests = new ArrayList<>();
        for (int i = 0; i < departureCities.size(); i++) {
            String departureIata = getIataCode(departureCities.get(i));
            String arrivalIata = getIataCode(arrivalCities.get(i));

            Mono<List<FlightOffersDTO>> flightMono = getAllFlightsFromAmadeus(
                    departureIata, arrivalIata, flightDates.get(i), flightDates.get(i).plusDays(1), userPreferences.getNumberOfAdults()
            );
            flightRequests.add(flightMono);
        }

        return Mono.zip(flightRequests, results -> {
            List<PlaneRide> planeRides = new ArrayList<>();
            for (Object result : results) {
                List<FlightOffersDTO> flightOffers = (List<FlightOffersDTO>) result;
                for (FlightOffersDTO offer : flightOffers) {
                    PlaneRide planeRide = savePlaneRideAndSegments(offer);
                    planeRides.add(planeRide);
                }
            }
            return planeRides;
        });
    }






    private List<FlightOffersDTO> convertToFlightOffersDTO(FlightDataDTO flightDataDTO) {
        List<FlightOffersDTO> flightOffers = new ArrayList<>();
        DictionnaryDTO dictionary = flightDataDTO.getDictionnary();

        for (FlightOffersDTO flightOffer : flightDataDTO.getFlightOffers()) {
            enrichFlightOffersWithNames(flightOffer, dictionary);
            flightOffers.add(flightOffer);
        }
        return flightOffers;
    }



    /*private Duration calculateTotalSegmentDuration(List<String> durations){
        Duration totalDuration = Duration.ZERO;
        for(String segmentDuration : durations){
            Duration duration = Duration.parse(segmentDuration);
            totalDuration = totalDuration.plus(duration);
        }

        long hours = totalDuration.toHours();
        long minutes = totalDuration.toMinutes() % 60;

        return totalDuration;

    }*/

    @Transactional
    public PlaneRide savePlaneRideAndSegments(FlightOffersDTO flightOffer) {
        // Créez le PlaneRide
        PlaneRide planeRide = new PlaneRide(
                flightOffer.isOneWay(),
                flightOffer.getPrice().getTotalPrice(),
                "EUR",
                LocalDateTime.now()
        );

        // Sauvegardez le PlaneRide
        planeRide = planeRideDao.save(planeRide);
        if (planeRide.getId() == null) {
            throw new IllegalStateException("L'insertion du PlaneRide a échoué, ID non généré.");
        }
        System.out.println("PlaneRide inséré avec l'ID: " + planeRide.getId());

        // Créez et sauvegardez les segments
        List<FlightSegmentRide> segmentRides = new ArrayList<>();
        for (FlightItineraryDTO itinerary : flightOffer.getItineraries()) {
            for (FlightSegmentDTO segmentDTO : itinerary.getSegments()) {
                Duration duration = Duration.parse(segmentDTO.getDuration());

                // Extraire l'heure et les minutes de la durée
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;

                // Sauvegarder le segment
                FlightSegment segment = new FlightSegment(
                        segmentDTO.getDeparture().getIataCode(),
                        segmentDTO.getArrival().getIataCode(),
                        segmentDTO.getDeparture().getDateTime(),
                        segmentDTO.getArrival().getDateTime(),
                        segmentDTO.getCarrierCode(),
                        segmentDTO.getCarrierName(),
                        segmentDTO.getAircraftCode().getCode(),
                        segmentDTO.getAircraftName(),
                        LocalTime.of((int) hours, (int) minutes)   // Assurez-vous que la durée est bien convertie
                );
                segment = flightSegmentDao.save(segment);
                if (segment.getId() == null) {
                    throw new IllegalStateException("L'insertion du FlightSegment a échoué, ID non généré.");
                }
                System.out.println("FlightSegment inséré avec l'ID: " + segment.getId());

                // Associez le segment au PlaneRide
                segmentRides.add(new FlightSegmentRide(planeRide.getId(), segment.getId()));
            }
        }

        // Sauvegardez les relations dans flightSegmentRide
        flightSegmentRideDao.saveAll(segmentRides);
        System.out.println("Tous les segments ont été associés au PlaneRide.");

        return planeRide;
    }

//    private PlaneRide savePlaneRideAndSegments(FlightOffersDTO flightOffer) {
//        // Créer le planeRide
//        PlaneRide planeRide = new PlaneRide(
//                flightOffer.isOneWay(),
//                flightOffer.getPrice().getTotalPrice(),
//                "EUR",
//                LocalDateTime.now()
//        );
//
//        // Sauvegarder le planeRide et récupérer son ID
//        planeRide = planeRideDao.save(planeRide);
//
//        if (planeRide.getId() == null) {
//            throw new IllegalStateException("L'insertion du PlaneRide a échoué, ID non généré.");
//        }
//
//        // Liste pour stocker les segmentRides
//        List<FlightSegmentRide> segmentRides = new ArrayList<>();
//
//        for (FlightItineraryDTO itinerary : flightOffer.getItineraries()) {
//            for (FlightSegmentDTO segmentDTO : itinerary.getSegments()) {
//
//                Duration duration = Duration.parse(segmentDTO.getDuration());
//
//                // Extraire l'heure et les minutes de la durée
//                long hours = duration.toHours();
//                long minutes = duration.toMinutes() % 60;
//
//                // Sauvegarder le segment
//                FlightSegment segment = new FlightSegment(
//                        segmentDTO.getDeparture().getIataCode(),
//                        segmentDTO.getArrival().getIataCode(),
//                        segmentDTO.getDeparture().getDateTime(),
//                        segmentDTO.getArrival().getDateTime(),
//                        segmentDTO.getCarrierCode(),
//                        segmentDTO.getCarrierName(),
//                        segmentDTO.getAircraftCode().getCode(),
//                        segmentDTO.getAircraftName(),
//                        LocalTime.of((int) hours, (int) minutes)   // Assurez-vous que la durée est bien convertie
//                );
//                segment = flightSegmentDao.save(segment);  // Sauvegarder le segment
//
//                if (segment.getId() == null) {
//                    throw new IllegalStateException("L'insertion du FlightSegment a échoué, ID non généré.");
//                }
//
//                // Associer le segment avec le PlaneRide (relation avec la table `flightSegmentRide`)
//                FlightSegmentRide segmentRide = new FlightSegmentRide(planeRide.getId(), segment.getId());
//                segmentRides.add(segmentRide);  // Ajouter la relation
//            }
//        }
//
//        // Sauvegarder toutes les relations dans `flightSegmentRide` après avoir inséré tous les segments
//        flightSegmentRideDao.saveAll(segmentRides);
//
//        // Retourner l'objet `planeRide` qui a été créé
//        return planeRide;
//    }



    private String getIataCode(String cityName) {
        City city = cityDao.findCityByName(cityName);
        return city.getIataCode();
    }

    private void enrichFlightOffersWithNames(FlightOffersDTO flightOffer, DictionnaryDTO dictionary) {
        for (FlightItineraryDTO itinerary : flightOffer.getItineraries()) {
            for (FlightSegmentDTO segment : itinerary.getSegments()) {
                // Récupérer le nom du transporteur depuis le dictionnaire
                if (dictionary.getCarriers().containsKey(segment.getCarrierCode())) {
                    segment.setCarrierName(dictionary.getCarriers().get(segment.getCarrierCode()));
                }

                // Récupérer le nom de l'avion depuis le dictionnaire
                if (segment.getAircraftCode() != null && dictionary.getAircraft().containsKey(segment.getAircraftCode().getCode())) {
                    segment.setAircraftName(dictionary.getAircraft().get(segment.getAircraftCode().getCode()));
                }
            }
        }
    }


}
