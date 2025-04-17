package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.dtos.userItinerary.DraftData;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.*;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.services.flight.PlaneRideService;
import com.example.odyssea.services.mainTables.HotelService;
import com.example.odyssea.services.userItinerary.helpers.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class UserDailyPlanService {
    private final UserItineraryStepDao userItineraryStepDao;
    private final CityDao cityDao;
    private final CountryDao countryDao;
    private final DayAssigner dayAssigner;
    private final LocationAssigner locationAssigner;
    private final HotelAssigner hotelAssigner;
    private final ActivityAssigner activityAssigner;
    private final FlightAssigner flightAssigner;


    public UserDailyPlanService(UserItineraryStepDao userItineraryStepDao, CityDao cityDao, CountryDao countryDao, DayAssigner dayAssigner, LocationAssigner locationAssigner, HotelAssigner hotelAssigner, ActivityAssigner activityAssigner, FlightAssigner flightAssigner) {
        this.userItineraryStepDao = userItineraryStepDao;
        this.cityDao = cityDao;
        this.countryDao = countryDao;
        this.dayAssigner = dayAssigner;
        this.locationAssigner = locationAssigner;
        this.hotelAssigner = hotelAssigner;
        this.activityAssigner = activityAssigner;
        this.flightAssigner = flightAssigner;
    }

    /*public UserItineraryDayDTO toUserItineraryStep(UserItinerary userItinerary, UserItineraryStep userItineraryStep) {
        // 1. Gestion de la ville et du pays
        String cityName = cityDao.findById(userItineraryStep.getCityId())
                .map(City::getName)
                .orElse("Ville inconnue"); // Fallback au lieu de throw

        String countryName = countryDao.findByCityName(cityName)
                .map(Country::getName)
                .orElse("Pays inconnu"); // Fallback au lieu de throw

        // 2. Calcul de la date
        LocalDate date = userItinerary.getStartDate().toLocalDate()
                .plusDays(userItineraryStep.getDayNumber() - 1); // -1 car les jours commencent à 1

        // 3. Gestion des hôtels
        HotelDto hotelDto = Optional.ofNullable(userItineraryStepDao.getHotelInADay(
                        userItinerary.getId(),
                        userItineraryStep.getDayNumber()))
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(HotelDto::fromEntity)
                .orElse(new HotelDto()); // Fallback

        // 4. Gestion des activités
        Activity activity = Optional.ofNullable(userItineraryStepDao.getActivitiesInADay(
                        userItinerary.getId(),
                        userItineraryStep.getDayNumber()))
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .orElse(new Activity()); // Fallback

        // 5. Construction du DTO
        return new UserItineraryDayDTO(
                cityName,
                countryName,
                hotelDto,
                activity,
                userItineraryStep.getDayNumber(),
                date,
                userItineraryStep.isOffDay(),
                new FlightItineraryDTO()
        );
    }*/

    public List<UserItineraryDayDTO> generateEachDay(DraftData draftData){
        int duration = draftData.getDraft().getDuration();
        AtomicInteger index = new AtomicInteger(0);

        return IntStream.range(1, duration + 1)
                .mapToObj(i ->createItineraryDay(draftData, i, index))
                .toList();
    }

    private UserItineraryDayDTO createItineraryDay(DraftData draftData, int dayNumber, AtomicInteger index) {
        UserItineraryDayDTO day = new UserItineraryDayDTO();
        List<Country> countries = draftData.getCountries();
        List<City> cities = draftData.getCities();
        List<Activity> activities = draftData.getActivities();
        //List<Hotel> hotels = hotelAssigner.getHotels(draftData.getDraft().getHotelStanding(), cities);
        int totalPeople = draftData.getDraft().getNumberAdults() + draftData.getDraft().getNumberKids();
        // Construction de la liste des villes visitées pour les vols
        LinkedList<City> visitedCities = new LinkedList<>(draftData.getCities());
        City departureCity = cityDao.findCityByName(draftData.getDraft().getDepartureCity());

        if (!visitedCities.getFirst().getName().equals(departureCity.getName())) {
            visitedCities.addFirst(departureCity);
        }
        if (!visitedCities.getLast().getName().equals(departureCity.getName())) {
            visitedCities.addLast(departureCity);
        }


        LocalDate date = dayAssigner.assignDate(draftData.getDraft().getStartDate(), dayNumber);
        day.setDayNumber(dayNumber);
        boolean isOff = dayAssigner.isDayOff(day);
        String countryName = locationAssigner.assignCountry(day, countries);
        String cityName = locationAssigner.assignCity(day, cities, draftData.getDraft().getDuration());
        Activity activity = activityAssigner.assignActivity(day, activities, index);
        //Hotel hotel = hotelAssigner.assignHotel(day, hotels);
        Mono<FlightItineraryDTO> flight = flightAssigner.assignFlight(day,visitedCities, totalPeople);
        //System.out.println("Flight : " + Objects.requireNonNull(flight.block()));

        day.setDate(date);
        day.setDayOff(isOff);
        day.setCountryName(countryName);
        day.setCityName(cityName);
        day.setActivity(activity);
        day.setHotel(null);
        day.setFlightItineraryDTO(flight.block());

        return day;
    }

}
