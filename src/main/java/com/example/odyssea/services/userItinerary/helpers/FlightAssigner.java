package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.services.flight.PlaneRideService;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class FlightAssigner {
    private final CityDao cityDao;
    private final PlaneRideService planeRideService;
    private static final Logger logger = LoggerFactory.getLogger(FlightAssigner.class);

    public FlightAssigner(CityDao cityDao, PlaneRideService planeRideService) {
        this.cityDao = cityDao;
        this.planeRideService = planeRideService;
    }

    public Mono<FlightItineraryDTO> assignFlight(UserItineraryDayDTO day, List<City> visitedCities, int totalPeople) {
        StopWatch watch = new StopWatch();
        watch.start("Assigning flight for a day");
        logger.info("Assigning flight for day: {}", day.getDayNumber());

        if (!day.isDayOff()) {
            return Mono.empty();
        }

        int dayOffIndex = getOffDayIndex(day.getDayNumber());

        if (dayOffIndex >= visitedCities.size() - 1) {
            logger.warn("DayOffIndex ({}) exceeds available cities ({}). No flight possible.", dayOffIndex, visitedCities.size());
            return Mono.empty();
        }

        City fromCity = visitedCities.get(dayOffIndex);
        City toCity = visitedCities.get(dayOffIndex + 1);
        LocalDate date = day.getDate();


        logger.info("Fetching flight from {} to {} on {}", fromCity.getName(), toCity.getName(), date);

        Mono<FlightItineraryDTO> flightAssigned = planeRideService.getFlights(
                        fromCity.getIataCode(),
                        toCity.getIataCode(),
                        date,
                        date,
                        totalPeople
                )
                .doOnNext(flights -> logger.info("Received {} flights", flights.size()))
                .flatMap(flights -> {
                    if (flights.isEmpty()) {
                        logger.warn("No flights found from {} to {} on {}", fromCity.getName(), toCity.getName(), date);
                        return Mono.empty();
                    }
                    FlightItineraryDTO selectedFlight = flights.getFirst();

                    return Mono.just(selectedFlight);
                })
                .doOnError(e -> new DatabaseException("Something went wrong while fetching flights" + e.getMessage()));
        watch.stop();
        System.out.println(watch.prettyPrint());

        return flightAssigned;
    }


    private int getOffDayIndex(int currentDayNumber) {
        int count = 0;
        for (int i = 1; i <= currentDayNumber; i++) {
            if ((i - 1) % 4 == 0) {
                count++;
            }
        }

        return count - 1;
    }

}
