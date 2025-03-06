package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.dtos.UserItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDailyPlanService {
    private final UserItineraryStepDao userItineraryStepDao;
    private final CityDao cityDao;
    private final CountryDao countryDao;


    public UserDailyPlanService(UserItineraryStepDao userItineraryStepDao, CityDao cityDao, CountryDao countryDao) {
        this.userItineraryStepDao = userItineraryStepDao;
        this.cityDao = cityDao;
        this.countryDao = countryDao;
    }

    public UserItineraryDayDTO toUserItineraryStep(UserItinerary userItinerary, UserItineraryStep userItineraryStep){
        String cityName = cityDao.findById(userItineraryStep.getCityId()).map(City::getName).orElseThrow(() -> new RuntimeException("City not found."));
        String countryName = countryDao.findByCityName(cityName).map(Country::getName).orElseThrow(() -> new RuntimeException("Country not found."));
        LocalDate date = userItinerary.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(userItineraryStep.getDayNumber());

        List<Hotel> hotels = userItineraryStepDao.getHotelInADay(userItinerary.getId(), userItineraryStep.getDayNumber());
        List<HotelDto> hotelDtos = new ArrayList<>();
        for(Hotel hotel : hotels){
            hotelDtos.add(HotelDto.fromEntity(hotel));
        }

        List<Activity> activities = userItineraryStepDao.getActivitiesInADay(userItinerary.getId(), userItineraryStep.getDayNumber());

        return new UserItineraryDayDTO(
                cityName,
                countryName,
                new ArrayList<>(), // Vols
                hotelDtos,
                activities,
                userItineraryStep.getDayNumber(),
                date,
                userItineraryStep.isOffDay()
        );
    }
}
