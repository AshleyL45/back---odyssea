package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.dtos.UserItinerary.*;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        userItinerary.setDepartureCityIata(cityDao.findCityByName(userPreferences.getDepartureCity()).getIataCode());
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
                // Assigner une activité
                assignCity(userItineraryDay, userPreferences.getCountrySelection().getFirst(), dayNumber);
                assignActivity(userItineraryDay, userPreferences.getCountrySelection().getFirst());
            } else if (dayNumber <= 8){
                assignCity(userItineraryDay, userPreferences.getCountrySelection().get(1), dayNumber);
                assignActivity(userItineraryDay, userPreferences.getCountrySelection().get(1));
            } else if (dayNumber <= 12){
                assignCity(userItineraryDay, userPreferences.getCountrySelection().getLast(), dayNumber);
                assignActivity(userItineraryDay, userPreferences.getCountrySelection().get(2));
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
            List<HotelDto> userItineraryDayHotels = assignHotel(userItineraryDay.getCityName(), userPreferences.getHotelStanding());
            if(userItineraryDay.getHotels() == null){
                userItineraryDay.setHotels(new ArrayList<>());
            }

            double random = Math.random() * userItineraryDayHotels.size();
            userItineraryDay.getHotels().add(userItineraryDayHotels.get((int) random));

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



    // Calculer le prix total
    private BigDecimal calculateTotal(List<BigDecimal> countriesPrices, List<BigDecimal> options, Integer numberOfAdults, Integer numberOfKids){

        BigDecimal totalCountriesPrices = countriesPrices.stream()
                .filter(Objects::nonNull)  // Filtre les valeurs nulles
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOptions = options.stream()
                .filter(Objects::nonNull)  // Filtre les valeurs nulles
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalPeople = (numberOfAdults != null ? numberOfAdults : 0) + (numberOfKids != null ? numberOfKids : 0);


        if (totalPeople == 0) {
            return BigDecimal.ZERO;
        }

        return totalCountriesPrices.add(totalOptions).multiply(BigDecimal.valueOf(totalPeople));
    }


    // Assigner la ville
    private void assignCity(UserItineraryDayDTO day, CountrySelectionDTO country, int dayNumber){
        if(country.getCitySelection().size() != 2){
            throw new RuntimeException("A country must have 2 cities");
        }

        day.setCountryName(country.getCountryName());

        if((dayNumber % 4) <= 2 && (dayNumber % 4) != 0){
            day.setCityName(country.getCitySelection().getFirst().getCityName());
        } else {
            day.setCityName(country.getCitySelection().get(1).getCityName());
        }

    }

    // Assigner un hôtel
    private List<HotelDto> assignHotel(String cityName, int starRating){
        List<Hotel> hotels = hotelService.getHotelsByCityAndStarRating(cityDao.findCityByName(cityName).getId(), starRating);
        if(hotels.isEmpty()){
            return null;
        }
        return hotels.stream()
                .map(HotelDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Assigner une activité
    private List<Activity> assignActivity(UserItineraryDayDTO day, CountrySelectionDTO countrySelection){
        if(day.isDayOff()){
            return null;
        }

        // Les activités du jour
        List<Activity> dayActivities = day.getActivities();
        //System.out.println("Day activities 1: " + dayActivities);
        if(dayActivities == null){
            dayActivities = new ArrayList<>();
            day.setActivities(dayActivities);
        }

        // Les villes du pays en question
        List<CitySelectionDTO> cities = countrySelection.getCitySelection();
        CitySelectionDTO firstCity = cities.getFirst();
        CitySelectionDTO secondCity = cities.get(1);

        // Vérifier s'il y a plus de deux activités dans une ville
        if(firstCity.getActivities().size() > 2 || secondCity.getActivities().size() > 2){
            throw new RuntimeException("There can't be more then 2 activities per city.");
        }

        if((day.getDayNumber() % 4) == 2){ // Si c'est le deuxième jour dans un pays
           dayActivities.add(firstCity.getActivities().getFirst());
        } else if((day.getDayNumber() % 4) == 3){ // Si c'est le troisième jour dans un pays
            dayActivities.add(secondCity.getActivities().getFirst());
        } else if (day.getDayNumber() % 4 == 0){ // Si c'est le quatrième jour dans un pays
            dayActivities.add(secondCity.getActivities().get(1));
        }

        return dayActivities;

    }


}
