package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.services.mainTables.HotelService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelAssigner {
    private  HotelService hotelService;

    public HotelAssigner(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    public Hotel assignHotel(UserItineraryDayDTO day, List<Hotel> hotelsList){
        int dayNumber = day.getDayNumber();
        int daysPerHotel = 4;

        int index = (dayNumber - 1) / daysPerHotel;

        if (index >= hotelsList.size()) {
            index = hotelsList.size() - 1;
        }

        return hotelsList.get(index);
    }

    public List<Hotel> getHotels (int startRating, List<City> cities){
        List<Hotel> hotels = new ArrayList<>();
        for (City city : cities ){
            hotels.add(hotelService.getHotelsByCityAndStarRating(city.getId(), startRating).getFirst());
        }

        return hotels;
    }
}
