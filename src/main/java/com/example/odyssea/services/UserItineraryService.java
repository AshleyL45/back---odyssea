package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.CountrySelectionDTO;
import com.example.odyssea.dtos.UserItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.UserItineraryDayDTO;
import com.example.odyssea.dtos.UserItinerary.UserPreferencesDTO;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Service
public class UserItineraryService {
    private UserItineraryDTO userItineraryDTO;
    private UserItineraryDayDTO userItineraryDayDTO;
    private FlightService flightService;
    private CityDao cityDao;


    public UserItineraryService() {
    }

    public UserItineraryService(UserItineraryDTO userItineraryDTO, UserItineraryDayDTO userItineraryDayDTO, FlightService flightService,CityDao cityDao) {
        this.userItineraryDTO = userItineraryDTO;
        this.userItineraryDayDTO = userItineraryDayDTO;
        this.flightService = flightService;
        this.cityDao = cityDao;
    }

    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) {
        UserItineraryDTO userItinerary = new UserItineraryDTO();

        userItinerary.setUserId(userPreferences.getUserId());
        //userItinerary.setStartingPrice(calculateTotal(userPreferences.getNumberOfAdults(), userPreferences.getNumberOfKids()));
        userItinerary.setDepartureDate(userPreferences.getStartDate());
        userItinerary.setDepartureCityIata(userPreferences.getDepartureCity()); // A modifier avec une fonction pour convertir des villes en code IATA
        userItinerary.setArrivalDate(userPreferences.getStartDate().plusDays(12));
        userItinerary.setArrivalCityIata(userPreferences.getDepartureCity());

        // Renvoyer les vols
        List<FlightItineraryDTO> flights = flightService.getFlights(
                userPreferences.getDepartureCity(),
                userPreferences.getCountrySelection().getFirst().getCitySelection().getFirst().getCityName(),
                userPreferences.getStartDate(),
                userPreferences.getStartDate(),
                userPreferences.getNumberOfAdults() + userPreferences.getNumberOfKids()
        ).block();

        // Créer les 12 jours du voyage
        List<UserItineraryDayDTO> userItineraryDays = IntStream.range(0, 12)
                .mapToObj(i -> new UserItineraryDayDTO())
                .collect(Collectors.toList());

        int i = 1;
        for(UserItineraryDayDTO userItineraryDay : userItineraryDays){
            userItineraryDay.setDayNumber(i);

            // Déterminer le pays en fonction du jour
            int dayNumber = userItineraryDay.getDayNumber();

            if(dayNumber <=  4){
                userItineraryDay.setCountryName(userPreferences.getCountrySelection().getFirst().getCountryName());
            } else if (dayNumber <= 8){
                userItineraryDay.setCountryName(userPreferences.getCountrySelection().get(1).getCountryName());
            } else if (dayNumber <= 12){
                userItineraryDay.setCountryName(userPreferences.getCountrySelection().getLast().getCountryName());
            } else {
                userItineraryDay.setCountryName(null);
            }


            // La ville du jour
            userItineraryDay.setCityName();



            userItineraryDay.setDate(userItinerary.getDepartureDate().plusDays(userItineraryDay.getDayNumber()));
            if(userItineraryDay.getDayNumber() == 1 || userItineraryDay.getDayNumber() == 5 || userItineraryDay.getDayNumber() == 9){
                userItineraryDay.setActivities(null);
            }
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
