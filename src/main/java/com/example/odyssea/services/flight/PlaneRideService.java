package com.example.odyssea.services.flight;

import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.flight.FlightSegmentRideDao;
import com.example.odyssea.dtos.flight.*;
import com.example.odyssea.entities.mainTables.FlightSegmentRide;
import com.example.odyssea.entities.mainTables.PlaneRide;
import com.example.odyssea.services.amadeus.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlaneRideService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneRideService.class);

    private final PlaneRideDao planeRideDao;
    private final FlightSegmentRideDao flightSegmentRideDao;
    private final TokenService tokenService;
    private final WebClient webClient;
    private final FlightSegmentService flightSegmentService;

    public PlaneRideService(PlaneRideDao planeRideDao,
                            FlightSegmentRideDao flightSegmentRideDao,
                            TokenService tokenService,
                            WebClient.Builder webClientBuilder,
                            FlightSegmentService flightSegmentService) {
        this.planeRideDao = planeRideDao;
        this.flightSegmentRideDao = flightSegmentRideDao;
        this.tokenService = tokenService;
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com/").build();
        this.flightSegmentService = flightSegmentService;
    }

    public Mono<List<FlightItineraryDTO>> getFlights(String departureIata, String arrivalIata,
                                                     LocalDate departureDate, LocalDate arrivalDate,
                                                     int totalPeople) {
        return tokenService.getValidToken()
                .flatMap(token -> fetchFlightOffers(departureIata, arrivalIata, departureDate, arrivalDate, totalPeople, token))
                .flatMap(this::processFlightData)
                .onErrorResume(e -> {
                    logger.error("Error fetching flights: {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                });
    }

    private Mono<FlightDataDTO> fetchFlightOffers(String departureIata, String arrivalIata,
                                                  LocalDate departureDate, LocalDate arrivalDate,
                                                  int totalPeople, String token) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("v2/shopping/flight-offers")
                        .queryParam("originLocationCode", departureIata)
                        .queryParam("destinationLocationCode", arrivalIata)
                        .queryParam("departureDate", departureDate.toString())
                        .queryParam("returnDate", arrivalDate.toString())
                        .queryParam("adults", totalPeople)
                        .queryParam("max", 2)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(FlightDataDTO.class);
    }

    private Mono<List<FlightItineraryDTO>> processFlightData(FlightDataDTO flightDataDTO) {
        if (flightDataDTO == null || flightDataDTO.getData() == null || flightDataDTO.getData().isEmpty()) {
            return Mono.just(Collections.emptyList());
        }

        FlightOffersDTO offer = flightDataDTO.getData().get(0);
        if (offer.getItineraries() == null || offer.getItineraries().isEmpty()) {
            return Mono.just(Collections.emptyList());
        }

        FlightItineraryDTO itinerary = offer.getItineraries().get(0);
        DictionnaryDTO dict = flightDataDTO.getDictionnary();

        // Process segments
        List<Integer> savedSegmentIds = processSegments(itinerary, dict);

        // Create and save plane ride
        BigDecimal totalPrice = getTotalPrice(offer);
        PlaneRide savedPlaneRide = createAndSavePlaneRide(offer, totalPrice);

        // Link segments to plane ride
        linkSegmentsToPlaneRide(savedSegmentIds, savedPlaneRide);

        // Build complete response DTO
        FlightItineraryDTO responseDto = buildResponseDto(itinerary, totalPrice, offer, savedPlaneRide);

        return Mono.just(Collections.singletonList(responseDto));
    }

    private List<Integer> processSegments(FlightItineraryDTO itinerary, DictionnaryDTO dict) {
        List<Integer> savedSegmentIds = new ArrayList<>();
        for (FlightSegmentDTO segmentDTO : itinerary.getSegments()) {
            enrichSegmentWithDictionaryData(segmentDTO, dict);
            FlightSegmentDTO savedSegment = flightSegmentService.save(segmentDTO);
            savedSegmentIds.add(Integer.parseInt(savedSegment.getId()));
        }
        return savedSegmentIds;
    }

    private void enrichSegmentWithDictionaryData(FlightSegmentDTO segment, DictionnaryDTO dict) {
        if (dict != null) {
            if (dict.getCarriers() != null && dict.getCarriers().containsKey(segment.getCarrierCode())) {
                segment.setCarrierName(dict.getCarriers().get(segment.getCarrierCode()));
            }
            if (dict.getAircraft() != null && segment.getAircraftCode() != null &&
                    dict.getAircraft().containsKey(segment.getAircraftCode().getCode())) {
                segment.setAircraftName(dict.getAircraft().get(segment.getAircraftCode().getCode()));
            }
        }
    }

    private BigDecimal getTotalPrice(FlightOffersDTO offer) {
        return (offer.getPrice() != null && offer.getPrice().getTotalPrice() != null)
                ? offer.getPrice().getTotalPrice()
                : BigDecimal.ZERO;
    }

    private PlaneRide createAndSavePlaneRide(FlightOffersDTO offer, BigDecimal totalPrice) {
        PlaneRideDTO planeRideDTO = createFlightDTO(offer);
        planeRideDTO.setTotalPrice(totalPrice);

        PlaneRide planeRideEntity = new PlaneRide(
                0,  // ID sera généré automatiquement
                true,
                planeRideDTO.getTotalPrice(),
                planeRideDTO.getCurrency(),
                null
        );

        PlaneRide saved = planeRideDao.save(planeRideEntity);
        logger.info("Saved PlaneRide with ID: {}", saved.getId());
        return saved;
    }

    private void linkSegmentsToPlaneRide(List<Integer> segmentIds, PlaneRide planeRide) {
        for (Integer segmentId : segmentIds) {
            FlightSegmentRide ride = new FlightSegmentRide(planeRide.getId(), segmentId);
            flightSegmentRideDao.save(ride);
        }
    }

    private FlightItineraryDTO buildResponseDto(FlightItineraryDTO itinerary,
                                                BigDecimal totalPrice,
                                                FlightOffersDTO offer,
                                                PlaneRide planeRide) {
        FlightItineraryDTO responseDto = new FlightItineraryDTO();
        responseDto.setId(planeRide.getId()); // Set the ID from saved entity
        responseDto.setSegments(itinerary.getSegments());
        //responseDto.setPrice(totalPrice);
        //responseDto.setCurrency(offer.getPrice() != null ? offer.getPrice().getCurrency() : "EUR");
        responseDto.setDuration(itinerary.getDuration());
        return responseDto;
    }

    private PlaneRideDTO createFlightDTO(FlightOffersDTO flightOffer) {
        if (flightOffer == null || flightOffer.getPrice() == null) {
            return new PlaneRideDTO(); // Return empty DTO instead of null
        }

        PlaneRideDTO dto = new PlaneRideDTO();
        dto.setCurrency(flightOffer.getPrice().getCurrency() != null
                ? flightOffer.getPrice().getCurrency()
                : "EUR");
        dto.setTotalPrice(BigDecimal.ZERO);
        return dto;
    }
}