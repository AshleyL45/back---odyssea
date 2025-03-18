package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.Flight.FlightOffersDTO;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.dtos.UserItinerary.*;
import com.example.odyssea.entities.mainTables.*;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.services.flight.PlaneRideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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
    private PlaneRideService planeRideService;
    private PlaneRideDao planeRideDao;

    public UserItineraryService() {

    }

    @Autowired
    public UserItineraryService(CityDao cityDao, HotelService hotelService, CountryDao countryDao, UserItineraryDao userItineraryDao, UserItineraryStepDao userItineraryStepDao, UserDailyPlanService userDailyPlanService, PlaneRideService planeRideService, PlaneRideDao planeRideDao) {
        this.cityDao = cityDao;
        this.hotelService = hotelService;
        this.countryDao = countryDao;
        this.userItineraryDao = userItineraryDao;
        this.userItineraryStepDao = userItineraryStepDao;
        this.userDailyPlanService = userDailyPlanService;
        this.planeRideService = planeRideService;
        this.planeRideDao = planeRideDao;
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }

    private void assignFlight(UserItineraryDayDTO day, String departureIata, String arrivalIata,
                              LocalDate departureDate, LocalDate arrivalDate, int totalPeople) {
        if (day.getDayNumber() == 1 || day.getDayNumber() == 5 || day.getDayNumber() == 9 || day.getDayNumber() == 13) {
            // Récupérer les vols pour ce jour
            Mono<List<FlightItineraryDTO>> flightsMono = planeRideService.getFlights(
                    departureIata, arrivalIata, departureDate, arrivalDate, totalPeople
            );

            // Bloquer pour obtenir le résultat
            List<FlightItineraryDTO> flights = flightsMono.block();

            if (flights != null && !flights.isEmpty()) {
                day.setPlaneRide(flights.get(0));
            }
        }
    }

    private PlaneRide saveFlight(FlightItineraryDTO flightItinerary, FlightOffersDTO flightOffersDTO) {
        // Convertir FlightItineraryDTO en PlaneRide
        PlaneRide planeRide = new PlaneRide();
        planeRide.setOneWay(true);
        planeRide.setTotalPrice(flightOffersDTO.getPrice().getTotalPrice());
        planeRide.setCurrency("EUR");

        // Sauvegarder l'entité PlaneRide dans la base de données
        return planeRideDao.save(planeRide);
    }

    public UserItinerary saveUserItinerary(UserItineraryDTO userItineraryDTO) {
        UserItinerary userItinerary = new UserItinerary();
        userItinerary.setUserId(userItineraryDTO.getUserId());
        userItinerary.setStartDate(Date.valueOf(userItineraryDTO.getStartDate()));
        userItinerary.setEndDate(Date.valueOf(userItineraryDTO.getEndDate()));
        userItinerary.setTotalDuration(userItineraryDTO.getDuration());
        userItinerary.setDepartureCity(userItineraryDTO.getDepartureCity());
        userItinerary.setStartingPrice(userItineraryDTO.getStartingPrice());
        userItinerary.setItineraryName(userItineraryDTO.getItineraryName());
        userItinerary.setNumberOfAdults(userItineraryDTO.getNumberOfAdults());
        userItinerary.setNumberOfKids(userItineraryDTO.getNumberOfKids());

        return userItineraryDao.save(userItinerary);
    }

    // Enregistrer chaque journée dans la BDD
    public void saveUserDailyPlans(UserItinerary userItinerary, List<UserItineraryDayDTO> days) {
        for (UserItineraryDayDTO dayDTO : days) {
            UserItineraryStep userItineraryStep = new UserItineraryStep();
            userItineraryStep.setUserId(userItinerary.getUserId());
            userItineraryStep.setUserItineraryId(userItinerary.getId());
            userItineraryStep.setHotelId(dayDTO.getHotel().getId());
            userItineraryStep.setCityId(cityDao.findCityByName(dayDTO.getCityName()).getId());
            userItineraryStep.setDayNumber(dayDTO.getDayNumber());
            userItineraryStep.setOffDay(dayDTO.isDayOff());
            userItineraryStep.setActivityId(dayDTO.getActivity().getId());

            // Sauvegarder l'ID du vol si présent
            if (dayDTO.getPlaneRide() != null) {
                userItineraryStep.setFlightId(dayDTO.getPlaneRide().getId());
            }

            // Sauvegarder le jour dans la base de données
            userItineraryStepDao.save(userItineraryStep);
        }
    }


    public UserItineraryDTO toUserItineraryDTO (UserItinerary userItinerary){
        LocalDate startDate = convertToLocalDate(userItinerary.getStartDate());
        LocalDate endDate = convertToLocalDate(userItinerary.getEndDate());

        List<UserItineraryStep> daysEntities = userItineraryStepDao.findDailyPlansOfAnItinerary(userItinerary.getId());
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

    @Transactional
    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) {
        UserItineraryDTO userItinerary = new UserItineraryDTO();
        userItinerary.setUserId(userPreferences.getUserId());
        userItinerary.setStartDate(userPreferences.getStartDate());
        userItinerary.setDepartureCity(cityDao.findCityByName(userPreferences.getDepartureCity()).getIataCode());
        userItinerary.setEndDate(userPreferences.getStartDate().plusDays(13));


        // Créer les 12 jours du voyage
        List<UserItineraryDayDTO> userItineraryDays = IntStream.range(0, 14)
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
                assignCity(userItineraryDay, userPreferences.getCountrySelection().getFirst(), dayNumber);
            }

            // Déterminer la date de chaque jour
            userItineraryDay.setDate(userItinerary.getStartDate().plusDays(userItineraryDay.getDayNumber()));

            // Vérifier si c'est un jour off (sans activités)
            if(userItineraryDay.getDayNumber() == 1 || userItineraryDay.getDayNumber() == 5 || userItineraryDay.getDayNumber() == 9){
                userItineraryDay.setDayOff(true);
                userItineraryDay.setActivity(null);
            }

            // Assigner un hôtel
            List<HotelDto> userItineraryDayHotels = assignHotel(userItineraryDay.getCityName(), userPreferences.getHotelStanding());
            if(userItineraryDay.getHotel() == null){
                userItineraryDay.setHotel(new HotelDto());
            }

            double random = Math.random() * userItineraryDayHotels.size();
            userItineraryDay.setHotel(userItineraryDayHotels.get((int) random));

            // Assigner les vols
            if (i == 1 || i == 5 || i == 9 || i == 13) {
                String departureIata = userItinerary.getDepartureCity();
                String arrivalIata = cityDao.findCityByName(userItineraryDay.getCityName()).getIataCode();
                LocalDate departureDate = userItineraryDay.getDate();
                LocalDate arrivalDate = departureDate;
                int totalPeople = userPreferences.getNumberOfAdults() + userPreferences.getNumberOfKids();

                // Récupérer les vols
                Mono<List<FlightItineraryDTO>> flightsMono = planeRideService.getFlights(
                        departureIata, arrivalIata, departureDate, arrivalDate, totalPeople
                );

                // Bloquer pour obtenir le résultat
                List<FlightItineraryDTO> flights = flightsMono.block();

                if (flights != null && !flights.isEmpty()) {
                    // On récupère simplement l'entité PlaneRide associée
                    FlightItineraryDTO flightItinerary = flights.get(0);

                    // Supposons que PlaneRideService retourne l'ID du PlaneRide sauvegardé
                    PlaneRide planeRide = planeRideDao.findById(flights.getFirst()); // Adaptez cette ligne selon votre implémentation

                    // Assigner le vol au jour
                    userItineraryDay.setPlaneRide(planeRide);
                }
            }

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

        // Sauvegarder l'itinéraire principal
        UserItinerary userItinerarySaved = saveUserItinerary(userItinerary);

        // Sauvegarder chaque jour du voyage
        saveUserDailyPlans(userItinerarySaved, userItinerary.getItineraryDays());

        // Retourner l'itinéraire DTO avec l'ID généré
        userItinerary.setId(userItinerarySaved.getId());

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
            System.out.println("City 1" + country.getCitySelection().getFirst().getCityName());
            System.out.println("Day number 1 :" + dayNumber);
        } else {
            day.setCityName(country.getCitySelection().get(1).getCityName());
            System.out.println("City 2" + country.getCitySelection().get(1).getCityName());
            System.out.println("Day number 2 :" + dayNumber);
        }

    }

    // Assigner un hôtel
    private List<HotelDto> assignHotel(String cityName, int starRating){
        System.out.println("City name : " + cityName);
        List<Hotel> hotels = hotelService.getHotelsByCityAndStarRating(cityDao.findCityByName(cityName).getId(), starRating);
        if(hotels.isEmpty()){
            return null;
        }
        return hotels.stream()
                .map(HotelDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Assigner une activité
    private Activity assignActivity(UserItineraryDayDTO day, CountrySelectionDTO countrySelection) {
        if (day.isDayOff()) {
            return null; // Aucune activité pour les jours de repos
        }

        // Les villes du pays en question
        List<CitySelectionDTO> cities = countrySelection.getCitySelection();
        CitySelectionDTO firstCity = cities.get(0);
        CitySelectionDTO secondCity = cities.get(1);

        // Vérifier s'il y a plus de deux activités dans une ville
        if (firstCity.getActivities().size() > 2 || secondCity.getActivities().size() > 2) {
            throw new RuntimeException("There can't be more than 2 activities per city.");
        }

        // Déterminer le jour dans le pays (1er, 2e, 3e ou 4e jour)
        int dayInCountry = (day.getDayNumber() - 1) % 4 + 1;

        // Assigner une activité en fonction du jour dans le pays
        Activity activity = null;
        if (dayInCountry == 2) {
            activity = firstCity.getActivities().getFirst();
        } else if (dayInCountry == 3) {
            activity = secondCity.getActivities().getFirst();
        } else if (dayInCountry == 4) {
            activity = secondCity.getActivities().get(1);
        }

        // Assigner l'activité au jour
        day.setActivity(activity);
        return activity;
    }


}