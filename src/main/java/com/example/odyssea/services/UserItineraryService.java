package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.dtos.UserItinerary.*;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class UserItineraryService {
    private CityDao cityDao;
    private HotelService hotelService;
    private CountryDao countryDao;
    private UserItineraryDao userItineraryDao;
    private UserItineraryStepDao userItineraryStepDao;
    private UserDailyPlanService userDailyPlanService;

    public UserItineraryService(UserItineraryStepDao userItineraryStepDao) {

    }

    @Autowired
    public UserItineraryService(CityDao cityDao, HotelService hotelService, CountryDao countryDao, UserItineraryDao userItineraryDao, UserItineraryStepDao userItineraryStepDao, UserDailyPlanService userDailyPlanService) {
        this.cityDao = cityDao;
        this.hotelService = hotelService;
        this.countryDao = countryDao;
        this.userItineraryDao = userItineraryDao;
        this.userItineraryStepDao = userItineraryStepDao;
        this.userDailyPlanService = userDailyPlanService;
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }


    public UserItineraryDTO toUserItineraryDTO (UserItinerary userItinerary){
        LocalDate startDate = convertToLocalDate(userItinerary.getStartDate());
        LocalDate endDate = convertToLocalDate(userItinerary.getEndDate());

        List<UserItineraryStep> daysEntities = userItineraryStepDao.findDailyPlansOfAnItinerary(userItinerary.getId());
        System.out.println("Steps found for itinerary " + userItinerary.getId() + ": " + daysEntities.size());
        List<UserItineraryDayDTO> days = new ArrayList<>();
        for(UserItineraryStep day : daysEntities){
            days.add( userDailyPlanService.toUserItineraryStep(userItinerary, day));

        }

        System.out.println("Itinerary ID: " + userItinerary.getId() + ", Days: " + days.size());


        return new UserItineraryDTO(
                userItinerary.getId(),
                userItinerary.getUserId(),
                startDate,
                endDate,
                userItinerary.getTotalDuration(),
                userItinerary.getDepartureCity(),
                userItinerary.getStartingPrice(),
                userItinerary.getItineraryName(),
                userItinerary.getNumberOfAdults(),
                userItinerary.getNumberOfKids(),
                days
        );

    }

    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) {
        UserItineraryDTO userItinerary = new UserItineraryDTO();
        userItinerary.setUserId(userPreferences.getUserId());
        userItinerary.setStartDate(userPreferences.getStartDate());
        userItinerary.setDepartureCity(cityDao.findCityByName(userPreferences.getDepartureCity()).getIataCode());
        userItinerary.setEndDate(userPreferences.getStartDate().plusDays(13));

        // Renvoyer les vols
       /* List<FlightItineraryDTO> flights = flightService.getFlights(
                userPreferences.getDepartureCity(),
                userPreferences.getCountrySelection().getFirst().getCitySelection().getFirst().getCityName(),
                userPreferences.getStartDate(),
                userPreferences.getStartDate(),
                userPreferences.getNumberOfAdults() + userPreferences.getNumberOfKids()
        ).block();*/


        // Créer les 12 jours du voyage
        List<UserItineraryDayDTO> userItineraryDays = IntStream.range(0, 13)
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
            userItineraryDay.setDate(userItinerary.getStartDate().plusDays(userItineraryDay.getDayNumber()));

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
        userItinerary.setDuration(13);
        userItinerary.setItineraryDays(userItineraryDays);

        // Sauvegarder dans la BDD puis récupérer l'id

        return userItinerary;
    }

    // Retourner la liste de tous les itinéraires d'un utilisateur
    public List<UserItineraryDTO> getAllUserItineraries(int userId){
        List<UserItinerary> userItineraries = userItineraryDao.findAllUserItineraries(userId);
        List<UserItineraryDTO> userItineraryDTOs = new ArrayList<>();

        for(UserItinerary userItinerary : userItineraries){
            userItineraryDTOs.add(toUserItineraryDTO(userItinerary));
        }
        System.out.println("Days of itinerary  all itineraries: " + userItineraryDTOs.size());
        return userItineraryDTOs;
    }


    // Retourner un itinéraire
    public UserItineraryDTO getAUserItineraryById(int userItineraryId){
        UserItinerary userItinerary = userItineraryDao.findById(userItineraryId);
        System.out.println("Days of itinerary : " + toUserItineraryDTO(userItinerary).getItineraryName());
        return toUserItineraryDTO(userItinerary);
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
