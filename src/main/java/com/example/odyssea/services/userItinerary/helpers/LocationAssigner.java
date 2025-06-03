package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.entities.mainTables.Country;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;

@Component
public class LocationAssigner {


    public String assignCountry(UserItineraryDayDTO day, List<Country> countryList) {
        StopWatch watch = new StopWatch();
        watch.start("Assigning country for a day");
        int dayNumber = day.getDayNumber();
        int daysPerCountry = 8;

        int countryIndex = (dayNumber - 1) / daysPerCountry;

        if (countryIndex >= countryList.size()) {
            countryIndex = countryList.size() - 1;
        }

        watch.stop();
        System.out.println(watch.prettyPrint());
        return countryList.get(countryIndex).getName();

    }

    public String assignCity(UserItineraryDayDTO day, List<City> cityList, int totalDays) {
        StopWatch watch = new StopWatch();
        watch.start("Assigning country for a day");
        int dayNumber = day.getDayNumber();

        int numberOfCities = cityList.size();
        int baseDaysPerCity = 4;
        int extraDays = totalDays % numberOfCities;

        int currentDay = 1;

        for (int i = 0; i < numberOfCities; i++) {

            int cityStayDuration = baseDaysPerCity + (i >= numberOfCities - extraDays ? 1 : 0); //4

            int startDay = currentDay;
            int endDay = currentDay + cityStayDuration - 1;

            if (dayNumber >= startDay && dayNumber <= endDay) {
                return cityList.get(i).getName();
            }

            currentDay += cityStayDuration;
        }

        watch.stop();
        System.out.println(watch.prettyPrint());
        return cityList.getLast().getName();
    }
}
