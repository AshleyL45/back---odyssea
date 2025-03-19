package com.example.odyssea.services;

import com.example.odyssea.daos.CityDao;
import com.example.odyssea.daos.CountryDao;
import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryOptionDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.Flight.FlightItineraryDTO;
import com.example.odyssea.dtos.Flight.FlightSegmentDTO;
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
    private UserItineraryOptionDao userItineraryOptionDao;

    public UserItineraryService() {

    }

    @Autowired
    public UserItineraryService(CityDao cityDao, HotelService hotelService, CountryDao countryDao, UserItineraryDao userItineraryDao, UserItineraryStepDao userItineraryStepDao, UserDailyPlanService userDailyPlanService, PlaneRideService planeRideService, PlaneRideDao planeRideDao, UserItineraryOptionDao userItineraryOptionDao) {
        this.cityDao = cityDao;
        this.hotelService = hotelService;
        this.countryDao = countryDao;
        this.userItineraryDao = userItineraryDao;
        this.userItineraryStepDao = userItineraryStepDao;
        this.userDailyPlanService = userDailyPlanService;
        this.planeRideService = planeRideService;
        this.planeRideDao = planeRideDao;
        this.userItineraryOptionDao = userItineraryOptionDao;
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }


    private PlaneRide findPlaneRideByItinerary(FlightItineraryDTO flightItinerary) {
        if (flightItinerary.getSegments() == null || flightItinerary.getSegments().isEmpty()) {
            return null;
        }

        // Prendre le premier segment pour la recherche (vous pouvez adapter cette logique)
        FlightSegmentDTO firstSegment = flightItinerary.getSegments().get(0);

        // Rechercher le PlaneRide correspondant dans la base de données
        List<PlaneRide> planeRides = planeRideDao.findBySegmentDetails(
                firstSegment.getDeparture().getIataCode(),
                firstSegment.getArrival().getIataCode(),
                firstSegment.getDeparture().getDateTime(),
                firstSegment.getArrival().getDateTime()
        );

        if (!planeRides.isEmpty()) {
            return planeRides.getFirst();
        }

        return null;
    }

    @Transactional
    public UserItinerary saveUserItinerary(UserItineraryDTO userItineraryDTO) throws Exception {

        UserItinerary userItinerary = new UserItinerary();

        userItinerary.setUserId(userItineraryDTO.getUserId());
        userItinerary.setStartDate(Date.valueOf(userItineraryDTO.getStartDate()));
        userItinerary.setEndDate(Date.valueOf(userItineraryDTO.getEndDate()));
        userItinerary.setTotalDuration(userItineraryDTO.getTotalDuration());
        userItinerary.setDepartureCity(userItineraryDTO.getDepartureCity());
        userItinerary.setStartingPrice(userItineraryDTO.getStartingPrice());
        userItinerary.setItineraryName(userItineraryDTO.getItineraryName());
        userItinerary.setNumberOfAdults(userItineraryDTO.getNumberOfAdults());
        userItinerary.setNumberOfKids(userItineraryDTO.getNumberOfKids());
        if (userItineraryDTO.getOptions() != null) {
            for (Option optionDTO : userItineraryDTO.getOptions()) {
                Option option = new Option();
                option.setName(optionDTO.getName());
                option.setPrice(optionDTO.getPrice());
                option.setDescription(optionDTO.getDescription());
                option.setCategory(optionDTO.getCategory());

                userItineraryOptionDao.save(userItinerary.getId(), option.getId());
            }
        }

        return userItineraryDao.save(userItinerary);
    }

    // Enregistrer chaque journée dans la BDD
    public void saveUserDailyPlans(UserItinerary userItinerary, List<UserItineraryDayDTO> days) {
        UserItinerary userItinerarySaved = userItineraryDao.findById(userItinerary.getId());
        System.out.println("Starting save user daily plan : " );

        int day = 1;
        for (UserItineraryDayDTO dayDTO : days) {

            UserItineraryStep userItineraryStep = new UserItineraryStep();
            userItineraryStep.setUserId(userItinerarySaved.getUserId());
            userItineraryStep.setUserItineraryId(userItinerarySaved.getId());
            userItineraryStep.setHotelId(dayDTO.getHotel().getId());

            int cityId = cityDao.findCityByName(dayDTO.getCityName()).getId();
            System.out.println("City ID: " + cityId);
            userItineraryStep.setCityId(cityId);

            System.out.println("Processing day: " + dayDTO.getDayNumber());
            userItineraryStep.setDayNumber(dayDTO.getDayNumber());

            System.out.println("Is off day: " + dayDTO.isDayOff());
            userItineraryStep.setOffDay(dayDTO.isDayOff());


            if (!dayDTO.isDayOff()) {
                if (dayDTO.getActivity() != null) {
                    System.out.println("Activity ID: " + dayDTO.getActivity().getId());
                    userItineraryStep.setActivityId(dayDTO.getActivity().getId());
                    userItineraryStep.setPlaneRideId(null);
                } else {
                    System.out.println("No activity for this day.");
                }
            }

            // Sauvegarder l'ID du vol si présent
            if (dayDTO.isDayOff()) {
                System.out.println("Plane ride present: " + dayDTO.getPlaneRide().getId());
                userItineraryStep.setPlaneRideId(dayDTO.getPlaneRide().getId());
                userItineraryStep.setActivityId(null);
            } else {
                System.out.println("No plane ride for this day.");
                userItineraryStep.setPlaneRideId(null);
            }

            //System.out.println("Plane ride of the day :" + dayDTO.getPlaneRide().getId());

            // Sauvegarder le jour dans la base de données
            System.out.println("Saving userItineraryStep: " + userItineraryStep);
            userItineraryStepDao.save(userItineraryStep);

            day++;
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

        List<Option> options = userItineraryOptionDao.findOptionsByUserItineraryId(userItinerary.getId());

        //System.out.println("Itinerary ID: " + userItinerary.getId() + ", Days: " + days.size());


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
                days,
                options

        );

    }

    @Transactional
    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) throws Exception {
        UserItineraryDTO userItinerary = new UserItineraryDTO();
        userItinerary.setUserId(userPreferences.getUserId());
        userItinerary.setStartDate(userPreferences.getStartDate());
        userItinerary.setDepartureCity(cityDao.findCityByName(userPreferences.getDepartureCity()).getIataCode());
        userItinerary.setEndDate(userPreferences.getStartDate().plusDays(13));


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
                assignCity(userItineraryDay, userPreferences.getCountrySelection().getFirst(), dayNumber);
            }

            // Déterminer la date de chaque jour
            userItineraryDay.setDate(userItinerary.getStartDate().plusDays(userItineraryDay.getDayNumber()));

            // Vérifier si c'est un jour off (sans activités)
            if(userItineraryDay.getDayNumber() == 1 || userItineraryDay.getDayNumber() == 5 || userItineraryDay.getDayNumber() == 9 || userItineraryDay.getDayNumber() == 13){
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
            if (userItineraryDay.isDayOff()) {
                String departureIata = userItinerary.getDepartureCity();
                String arrivalIata = cityDao.findCityByName(userItineraryDay.getCityName()).getIataCode();

                LocalDate departureDate = userItineraryDay.getDate();
                LocalDate arrivalDate = departureDate;

                System.out.println("Processing flight for day: " + i);
                System.out.println("Departure IATA: " + departureIata + ", Arrival IATA: " + arrivalIata);
                System.out.println("Departure Date: " + departureDate + ", Arrival Date: " + arrivalDate);

                int totalPeople = userPreferences.getNumberOfAdults() + userPreferences.getNumberOfKids();
                System.out.println("Total number of people: " + totalPeople);

                // Récupérer les vols via PlaneRideService
                Mono<List<FlightItineraryDTO>> flightsMono = planeRideService.getFlights(
                        departureIata, arrivalIata, departureDate, arrivalDate, totalPeople
                );

                // Bloquer pour obtenir le résultat
                List<FlightItineraryDTO> flights = flightsMono.block();

                if (flights != null) {
                    System.out.println("Flights sans block : " + flightsMono);
                    System.out.println("Number of flights found: " + flights.size());
                } else {
                    System.out.println("No flights found.");
                }

                if (flights != null && !flights.isEmpty()) {
                    // Trouver le PlaneRide correspondant dans la base de données
                    PlaneRide planeRide = findPlaneRideByItinerary(flights.get(0));

                    if (planeRide != null) {
                        System.out.println("Plane ride assigned for day " + i + ": " + planeRide.getId());
                        userItineraryDay.setPlaneRide(planeRide);
                    } else {
                        System.out.println("No matching PlaneRide found in the database for day " + i);
                    }
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
        userItinerary.setTotalDuration(13);
        userItinerary.setItineraryDays(userItineraryDays);
        userItinerary.setNumberOfAdults(userPreferences.getNumberOfAdults());
        userItinerary.setNumberOfKids(userPreferences.getNumberOfKids());
        userItinerary.setItineraryName(userPreferences.getItineraryName());

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