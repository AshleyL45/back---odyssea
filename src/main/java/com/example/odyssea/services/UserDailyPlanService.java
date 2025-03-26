package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.dtos.UserItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.*;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserDailyPlanService {
    private final UserItineraryStepDao userItineraryStepDao;
    private final CityDao cityDao;
    private final CountryDao countryDao;
    private final PlaneRideDao planeRideDao;


    public UserDailyPlanService(UserItineraryStepDao userItineraryStepDao, CityDao cityDao, CountryDao countryDao, PlaneRideDao planeRideDao) {
        this.userItineraryStepDao = userItineraryStepDao;
        this.cityDao = cityDao;
        this.countryDao = countryDao;
        this.planeRideDao = planeRideDao;
    }

    public UserItineraryDayDTO toUserItineraryStep(UserItinerary userItinerary, UserItineraryStep userItineraryStep) {
        // 1. Gestion de la ville et du pays
        String cityName = cityDao.findById(userItineraryStep.getCityId())
                .map(City::getName)
                .orElse("Ville inconnue"); // Fallback au lieu de throw

        String countryName = countryDao.findByCityName(cityName)
                .map(Country::getName)
                .orElse("Pays inconnu"); // Fallback au lieu de throw

        // 2. Calcul de la date
        LocalDate date = userItinerary.getStartDate().toLocalDate()
                .plusDays(userItineraryStep.getDayNumber() - 1); // -1 car les jours commencent à 1

        // 3. Gestion des hôtels
        HotelDto hotelDto = Optional.ofNullable(userItineraryStepDao.getHotelInADay(
                        userItinerary.getId(),
                        userItineraryStep.getDayNumber()))
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(HotelDto::fromEntity)
                .orElse(new HotelDto()); // Fallback

        // 4. Gestion des activités
        Activity activity = Optional.ofNullable(userItineraryStepDao.getActivitiesInADay(
                        userItinerary.getId(),
                        userItineraryStep.getDayNumber()))
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .orElse(new Activity()); // Fallback

        // 5. Construction du DTO
        return new UserItineraryDayDTO(
                cityName,
                countryName,
                hotelDto,
                activity,
                userItineraryStep.getDayNumber(),
                date,
                userItineraryStep.isOffDay(),
                new FlightItineraryDTO()
        );
    }
}
