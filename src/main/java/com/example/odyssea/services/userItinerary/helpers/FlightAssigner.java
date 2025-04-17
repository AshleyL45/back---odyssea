package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.services.flight.PlaneRideService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class FlightAssigner {
    private final CityDao cityDao;
    private final PlaneRideService planeRideService;

    public FlightAssigner(CityDao cityDao, PlaneRideService planeRideService) {
        this.cityDao = cityDao;
        this.planeRideService = planeRideService;
    }

    public Mono<FlightItineraryDTO> assignFlight(UserItineraryDayDTO day, List<City> visitedCities, int totalPeople) {
        if (!day.isDayOff()) {
            return Mono.empty();
        }

        int dayOffIndex = getOffDayIndex(day.getDayNumber());

        if (dayOffIndex >= visitedCities.size() - 1) {
            return Mono.empty();
        }

        City fromCity = visitedCities.get(dayOffIndex);
        City toCity = visitedCities.get(dayOffIndex + 1);
        LocalDate date = day.getDate();

        return planeRideService.getFlights(
                fromCity.getIataCode(),
                toCity.getIataCode(),
                date,
                date,
                totalPeople
        ).flatMap(flights -> {
            if (flights.isEmpty()) return Mono.empty();
            return Mono.just(flights.getFirst());
        });
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
