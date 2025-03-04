package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.dtos.UserItinerary.*;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Service
public class UserItineraryService {
    private CityDao cityDao;
    private HotelService hotelService;
    private CountryDao countryDao;


    public UserItineraryService() {
    }

    @Autowired
    public UserItineraryService(CityDao cityDao, HotelService hotelService, CountryDao countryDao) {
        this.cityDao = cityDao;
        this.hotelService = hotelService;
        this.countryDao = countryDao;
    }

    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) {
        UserItineraryDTO userItinerary = new UserItineraryDTO();

        userItinerary.setUserId(userPreferences.getUserId());
        userItinerary.setDepartureDate(userPreferences.getStartDate());
        userItinerary.setDepartureCityIata(userPreferences.getDepartureCity()); // A modifier avec une fonction pour convertir des villes en code IATA
        userItinerary.setArrivalDate(userPreferences.getStartDate().plusDays(12));

        // Renvoyer les vols
       /* List<FlightItineraryDTO> flights = flightService.getFlights(
                userPreferences.getDepartureCity(),
                userPreferences.getCountrySelection().getFirst().getCitySelection().getFirst().getCityName(),
                userPreferences.getStartDate(),
                userPreferences.getStartDate(),
                userPreferences.getNumberOfAdults() + userPreferences.getNumberOfKids()
        ).block();*/


        // Créer les 12 jours du voyage
        List<UserItineraryDayDTO> userItineraryDays = IntStream.range(0, 12)
                .mapToObj(i -> new UserItineraryDayDTO())
                .collect(Collectors.toList());


        // Constituer le programme du jour
        int i = 1;
        for(UserItineraryDayDTO userItineraryDay : userItineraryDays){
            userItineraryDay.setDayNumber(i);

            if (userPreferences.getCountrySelection().size() < 3) {
                throw new IllegalArgumentException("Il faut au moins 3 pays sélectionnés.");
            }

            // Déterminer le pays et la ville en fonction du jour
            int dayNumber = userItineraryDay.getDayNumber();

            if(dayNumber <=  4){
                assignCity(userItineraryDay, userPreferences.getCountrySelection().getFirst(), dayNumber);
            } else if (dayNumber <= 8){
                assignCity(userItineraryDay, userPreferences.getCountrySelection().get(1), dayNumber);
            } else if (dayNumber <= 12){
                assignCity(userItineraryDay, userPreferences.getCountrySelection().getLast(), dayNumber);
            } else {
                userItineraryDay.setCountryName(null);
            }

            // Déterminer la date de chaque jour
            userItineraryDay.setDate(userItinerary.getDepartureDate().plusDays(userItineraryDay.getDayNumber()));

            // Vérifier si c'est un jour off (sans activités)
            if(userItineraryDay.getDayNumber() == 1 || userItineraryDay.getDayNumber() == 5 || userItineraryDay.getDayNumber() == 9){
                userItineraryDay.setDayOff(true);
                userItineraryDay.setActivities(null);
            }

            // Assigner un hôtel
            /*List<HotelDto> userItineraryDayHotel = assignHotel(userItineraryDay.getCityName(), userPreferences.getHotelStanding());
            userItineraryDay.getHotels().add(userItineraryDayHotel.get(0));*/

            userItineraryDay.setFlights(null);

            i += 1;
        }

        List<BigDecimal> countriesPrices = userPreferences.getCountrySelection()
                .stream()
                .map(country -> Optional.ofNullable(countryDao.findByName(country.getCountryName()))
                        .map(Country::getPrice)
                        .orElse(BigDecimal.ZERO))
                .collect(Collectors.toList());

        List<BigDecimal> optionsPrices = userPreferences.getOptions()
                        .stream()
                                .map(Option::getPrice)
                                        .collect(Collectors.toList());

        userItinerary.setStartingPrice(calculateTotal(countriesPrices, optionsPrices, userPreferences.getNumberOfAdults(), userPreferences.getNumberOfKids()));
        userItinerary.setDuration(12);
        userItinerary.setItineraryDays(userItineraryDays);

        return userItinerary;
    }




    private BigDecimal calculateTotal(List<BigDecimal> countriesPrices, List<BigDecimal> options, Integer numberOfAdults, Integer numberOfKids){
        return Stream.concat(
                    countriesPrices.stream(),
                    options.stream()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(numberOfAdults + numberOfKids));
    }

    private void assignCity(UserItineraryDayDTO day, CountrySelectionDTO country, int dayNumber){
        if(country.getCitySelection().size() != 2){
            throw new RuntimeException("A country must have 2 cities");
        }

        day.setCountryName(country.getCountryName());

        if((dayNumber % 4) <= 2 && (dayNumber % 4) != 0){
            day.setCityName(country.getCitySelection().get(0).getCityName());
        } else {
            day.setCityName(country.getCitySelection().get(1).getCityName());
        }

    }

    /*private List<HotelDto> assignHotel(String cityName, int starRating){
        List<Hotel> hotels = hotelService.getHotelsByCityAndStarRating(cityDao.findCityByName(cityName).getId(), starRating);
        if(hotels.isEmpty()){
            return null;
        }
        return hotels.stream()
                .map(HotelDto::fromEntity)
                .collect(Collectors.toList());
    }*/


}
