package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.HotelNotFound;
import com.example.odyssea.services.mainTables.HotelService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class HotelAssigner {
    private final HotelService hotelService;
    private final CityDao cityDao;

    public HotelAssigner(HotelService hotelService, CityDao cityDao) {
        this.hotelService = hotelService;
        this.cityDao = cityDao;
    }

    public HotelDto assignHotel(UserItineraryDayDTO day, List<HotelDto> hotelsList){
        if(hotelsList == null || hotelsList.isEmpty()){
            throw new HotelNotFound("The list of hotels is empty.");
        }

        String cityOfTheDay = day.getCityName();
        int cityOfTheDayId = cityDao.findCityByName(cityOfTheDay).getId();

        HotelDto dayHotel = hotelsList.stream()
                .filter(hotelDto -> hotelDto.getCityId() == cityOfTheDayId)
                .findFirst()
                .orElseThrow(() -> new HotelNotFound("No hotel found for city : " + day.getCityName()));

        return dayHotel;
    }

    public Mono<List<HotelDto>> getHotels(int startRating, List<City> cities) {
        return Flux.zip(
                        Flux.fromIterable(cities),
                        Flux.interval(Duration.ofSeconds(1))
                )
                .flatMap(tuple -> {
                    City city = tuple.getT1();
                    return hotelService.fetchAndSaveHotelWithStarFromAmadeusByCity(
                            city.getIataCode(), city.getId(), startRating);
                })
                .collectList();
    }


}
