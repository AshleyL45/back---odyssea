package com.example.odyssea.services.userItinerary;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.services.userItinerary.helpers.LocationAssigner;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActivityAssignerTest {

    @Test
    void testAssignCityForMiddleDay(){
        City city1 = new City(1, 2, "Abidjan", "ABD", 82.2250, 52.8522);
        City city2 = new City(2, 4, "Rome", "ROM", 14.2250, 27.8522);

        List<City> cities = List.of(city1, city2);
        UserItineraryDayDTO day = new UserItineraryDayDTO(); // Jour 1
        LocationAssigner locationAssigner = new LocationAssigner();
        day.setDayNumber(5);

        String assignedCity = locationAssigner.assignCity(day, cities, 9);
        assertEquals("Rome", assignedCity);
    }

}
