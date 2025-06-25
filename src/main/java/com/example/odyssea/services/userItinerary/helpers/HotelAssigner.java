package com.example.odyssea.services.userItinerary.helpers;

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
    private  HotelService hotelService;

    public HotelAssigner(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    public HotelDto assignHotel(UserItineraryDayDTO day, List<HotelDto> hotelsList){
        int dayNumber = day.getDayNumber();
        int daysPerHotel = 4;

        int index = (dayNumber - 1) / daysPerHotel;
        if(hotelsList.isEmpty()){
            throw new HotelNotFound("The list of hotels is empty.");
        }

        System.out.println("Hotel length : " + hotelsList.size());

        if (index >= hotelsList.size()) {
            index = hotelsList.size() - 1;
        }

        return hotelsList.get(index);
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
