package com.example.odyssea.services;

import com.example.odyssea.dtos.UserItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.UserItineraryDayDTO;
import com.example.odyssea.dtos.UserItinerary.UserPreferencesDTO;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Service
public class UserItineraryService {
    private UserItineraryDTO userItineraryDTO;
    private UserItineraryDayDTO userItineraryDayDTO;


    public UserItineraryService() {
    }

    public UserItineraryService(UserItineraryDTO userItineraryDTO, UserItineraryDayDTO userItineraryDayDTO) {
        this.userItineraryDTO = userItineraryDTO;
        this.userItineraryDayDTO = userItineraryDayDTO;
    }

    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) {
        UserItineraryDTO userItinerary = new UserItineraryDTO();

        userItinerary.setUserId(userPreferences.getUserId());
        //userItinerary.setStartingPrice(calculateTotal(BigDecimal k)); A modifier avec la fonction calculateTotal
        userItinerary.setDepartureDate(userPreferences.getStartDate());
        userItinerary.setDepartureCityIata(userPreferences.getDepartureCity()); // A modifier avec une fonction pour convertir des villes en code IATA
        userItinerary.setArrivalDate(userPreferences.getStartDate().plusDays(12));
        userItinerary.setArrivalCityIata(userPreferences.getDepartureCity());

        List<UserItineraryDayDTO> userItineraryDays = IntStream.range(0, 12)
                .mapToObj(i -> new UserItineraryDayDTO())
                .collect(Collectors.toList());

        int i = 1;
        for(UserItineraryDayDTO userItineraryDay : userItineraryDays){
            userItineraryDay.setDayNumber(i);
            userItineraryDay.setDate(userItinerary.getDepartureDate().plusDays(userItineraryDay.getDayNumber()));
            /*if(userItineraryDay.getDayNumber() == 1 || userItineraryDay.getDayNumber() == 4 || userItineraryDay.getDayNumber() == 8 || userItineraryDay.getDayNumber() == 12){
                userItineraryDay.setActivities(null)
            }*/
            userItineraryDay.setFlights(null);
            userItineraryDay.setCountryName(null);
            i += 1;
        }

        userItinerary.setItineraryDays(userItineraryDays);

        return userItinerary;
    }

    private BigDecimal calculateTotal(List<BigDecimal> countriesPrices, List<Option> options, Integer numberOfAdults, Integer numberOfKids){
        return Stream.concat(
                    countriesPrices.stream(),
                    options.stream().map(Option::getPrice)
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(numberOfAdults + numberOfKids));
    }



}
