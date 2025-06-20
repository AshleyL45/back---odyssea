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
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

@Service
public class PlaneRideService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneRideService.class);

    private final PlaneRideDao planeRideDao;
    private final FlightSegmentRideDao flightSegmentRideDao;
    private final TokenService tokenService;
    private final WebClient webClient;
    private final FlightSegmentService flightSegmentService;
    private final Semaphore flightSemaphore = new Semaphore(10);


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
        StopWatch sw = new StopWatch();
        sw.start("getFlights");

        return tokenService.getValidToken()
                .flatMap(token -> fetchFlightOffers(departureIata, arrivalIata, departureDate, arrivalDate, totalPeople, token))
                .flatMap(this::processFlightData)
                .doOnSuccess(res -> {
                    sw.stop();
                    logger.info("getFlights terminé - Durée totale : {}s", sw.getTotalTimeSeconds());
                    System.out.println(sw.prettyPrint());
                })
                .onErrorResume(e -> {
                    logger.error("Erreur lors de getFlights : {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                });
    }

    private Mono<FlightDataDTO> fetchFlightOffers(String departureIata, String arrivalIata,
                                                  LocalDate departureDate, LocalDate arrivalDate,
                                                  int totalPeople, String token) {
        StopWatch sw = new StopWatch();

        return Mono.fromCallable(() -> {
                    flightSemaphore.acquire();
                    return true;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(ignored -> {
                    sw.start("🌐 Appel API Amadeus");
                    logger.info("Appel à Amadeus lancé...");

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
                            .bodyToMono(FlightDataDTO.class)
                            .doOnSuccess(response -> {
                                sw.stop();
                                logger.info("Réponse Amadeus reçue - {}s", sw.getTotalTimeSeconds());
                                System.out.println(sw.prettyPrint());
                            })
                            .doFinally(signal -> {
                                flightSemaphore.release(); // libère la place pour d'autres appels
                            });
                });
    }


    private Mono<List<FlightItineraryDTO>> processFlightData(FlightDataDTO flightDataDTO) {
        StopWatch sw = new StopWatch();
        sw.start("processFlightData");

        if (flightDataDTO == null || flightDataDTO.getData() == null || flightDataDTO.getData().isEmpty()) {
            sw.stop();
            logger.warn("Données Amadeus vides");
            System.out.println(sw.prettyPrint());
            return Mono.just(Collections.emptyList());
        }

        FlightOffersDTO offer = flightDataDTO.getData().get(0);
        if (offer.getItineraries() == null || offer.getItineraries().isEmpty()) {
            sw.stop();
            logger.warn("Pas d’itinéraires trouvés");
            System.out.println(sw.prettyPrint());
            return Mono.just(Collections.emptyList());
        }

        FlightItineraryDTO itinerary = offer.getItineraries().get(0);
        DictionnaryDTO dict = flightDataDTO.getDictionnary();

        // Chrono des segments
        StopWatch swSeg = new StopWatch();
        swSeg.start("processSegments");
        List<Integer> savedSegmentIds = processSegments(itinerary, dict);
        swSeg.stop();
        System.out.println(swSeg.prettyPrint());

        // Chrono sauvegarde plane ride
        StopWatch swSave = new StopWatch();
        swSave.start("savePlaneRide");
        BigDecimal totalPrice = getTotalPrice(offer);
        PlaneRide savedPlaneRide = createAndSavePlaneRide(offer, totalPrice);
        swSave.stop();
        System.out.println(swSave.prettyPrint());

        // Chrono lien segments ↔ plane ride
        StopWatch swLink = new StopWatch();
        swLink.start("linkSegmentsToPlaneRide");
        linkSegmentsToPlaneRide(savedSegmentIds, savedPlaneRide);
        swLink.stop();
        System.out.println(swLink.prettyPrint());

        // Chrono construction DTO final
        StopWatch swBuild = new StopWatch();
        swBuild.start("📦 buildDTO");
        FlightItineraryDTO responseDto = buildResponseDto(itinerary, totalPrice, offer, savedPlaneRide);
        swBuild.stop();
        System.out.println(swBuild.prettyPrint());

        sw.stop();
        System.out.println(sw.prettyPrint());
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
                0,
                true,
                planeRideDTO.getTotalPrice(),
                planeRideDTO.getCurrency(),
                null
        );

        return planeRideDao.save(planeRideEntity);
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
        responseDto.setId(planeRide.getId());
        responseDto.setSegments(itinerary.getSegments());
        responseDto.setDuration(itinerary.getDuration());
        return responseDto;
    }

    private PlaneRideDTO createFlightDTO(FlightOffersDTO flightOffer) {
        if (flightOffer == null || flightOffer.getPrice() == null) {
            return new PlaneRideDTO();
        }

        PlaneRideDTO dto = new PlaneRideDTO();
        dto.setCurrency(flightOffer.getPrice().getCurrency() != null
                ? flightOffer.getPrice().getCurrency()
                : "EUR");
        dto.setTotalPrice(BigDecimal.ZERO);
        return dto;
    }
}