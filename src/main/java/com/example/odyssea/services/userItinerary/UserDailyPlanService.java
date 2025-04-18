package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.dtos.userItinerary.DraftData;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.*;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.CityNotFound;
import com.example.odyssea.exceptions.CountryNotFound;
import com.example.odyssea.exceptions.UserItineraryDatabaseException;
import com.example.odyssea.services.flight.PlaneRideService;
import com.example.odyssea.services.mainTables.HotelService;
import com.example.odyssea.services.userItinerary.helpers.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserItineraryDao userItineraryDao;
    private final PlaneRideDao planeRideDao;


    public UserDailyPlanService(UserItineraryStepDao userItineraryStepDao, CityDao cityDao, CountryDao countryDao, DayAssigner dayAssigner, LocationAssigner locationAssigner, HotelAssigner hotelAssigner, ActivityAssigner activityAssigner, FlightAssigner flightAssigner, UserItineraryDao userItineraryDao, PlaneRideDao planeRideDao) {
        this.userItineraryStepDao = userItineraryStepDao;
        this.cityDao = cityDao;
        this.countryDao = countryDao;
        this.dayAssigner = dayAssigner;
        this.locationAssigner = locationAssigner;
        this.hotelAssigner = hotelAssigner;
        this.activityAssigner = activityAssigner;
        this.flightAssigner = flightAssigner;
        this.userItineraryDao = userItineraryDao;
        this.planeRideDao = planeRideDao;
    }

    @Transactional
    public void saveUserDailyPlans(UserItinerary userItinerary, List<UserItineraryDayDTO> days) {
        UserItinerary savedItinerary = userItineraryDao.findById(userItinerary.getId());

        days.stream()
                .map(dayDTO -> buildUserItineraryStep(savedItinerary, dayDTO))
                .forEach(userItineraryStepDao::save);
    }

    private UserItineraryStep buildUserItineraryStep(UserItinerary itinerary, UserItineraryDayDTO dayDTO) {
        UserItineraryStep dailyPlan = new UserItineraryStep();

        dailyPlan.setUserId(itinerary.getUserId());
        dailyPlan.setUserItineraryId(itinerary.getId());
        dailyPlan.setDayNumber(dayDTO.getDayNumber());
        dailyPlan.setOffDay(dayDTO.isDayOff());

        dailyPlan.setCityId(resolveCityId(dayDTO.getCityName()));
        dailyPlan.setHotelId(resolveHotelId(dayDTO));
        dailyPlan.setActivityId(resolveActivityId(dayDTO));
        dailyPlan.setPlaneRideId(resolvePlaneRideId(dayDTO));

        return dailyPlan;
    }

    private Integer resolveCityId(String cityName) {
        City city = cityDao.findCityByName(cityName);

        if(city != null){
            return city.getId();
        } else {
            throw new CityNotFound("City " + cityName + " was not found.");
        }
    }

    private Integer resolveHotelId(UserItineraryDayDTO dayDTO) {
        if(dayDTO.getHotel() != null){
            return dayDTO.getHotel().getId();
        }
        throw new UserItineraryDatabaseException("Something went wrong while saving the hotels in database." );
    }

    private Integer resolveActivityId(UserItineraryDayDTO dayDTO) {
        if (!dayDTO.isDayOff() && dayDTO.getActivity() != null) {
            return dayDTO.getActivity().getId();
        } else if (!dayDTO.isDayOff() && dayDTO.getActivity() == null){
            throw new UserItineraryDatabaseException("Something went wrong while saving the activity." );
        }

        return null;
    }

    private Integer resolvePlaneRideId(UserItineraryDayDTO dayDTO) {
        if (!dayDTO.isDayOff()) return null;

        try {
            FlightItineraryDTO flight = dayDTO.getFlightItineraryDTO();
            if (flight != null && flight.getId() != null && planeRideDao.existsById(flight.getId())) {
                return flight.getId();
            }
        } catch (Exception e) {
            throw new UserItineraryDatabaseException("Something went wrong while saving the flight : " + e.getMessage());
        }
        return null;
    }



    public UserItineraryDayDTO toUserItineraryDay (UserItinerary userItinerary, UserItineraryStep userItineraryStep) {

        String cityName = cityDao.findById(userItineraryStep.getCityId())
                .map(City::getName)
                .orElseThrow(() -> new CityNotFound("City with ID " + userItineraryStep.getCityId() + " has not been found"));


        String countryName = countryDao.findByCityName(cityName)
                .map(Country::getName)
                .orElseThrow(() -> new CountryNotFound("Country with name " + cityName + "has not been found"));

        LocalDate date = userItinerary.getStartDate()
                .plusDays(userItineraryStep.getDayNumber() - 1);

        Hotel hotel = userItineraryStepDao.getHotelInADay(userItinerary.getId(), userItineraryStep.getDayNumber());
        HotelDto hotelDto = HotelDto.fromEntity(hotel);


        Activity activity = userItineraryStepDao.getActivityInADay(userItinerary.getId(), userItineraryStep.getDayNumber());

        Integer planeRideId = userItineraryStep.getPlaneRideId();
        FlightItineraryDTO flightItineraryDTO = planeRideDao.getPlaneRideById(planeRideId);



        return new UserItineraryDayDTO(
                cityName,
                countryName,
                hotelDto,
                activity,
                userItineraryStep.getDayNumber(),
                date,
                userItineraryStep.isOffDay(),
                flightItineraryDTO
        );
    }

    public List<UserItineraryDayDTO> generateDailyPlan(DraftData draftData){
        int duration = draftData.getDraft().getDuration();
        AtomicInteger index = new AtomicInteger(0);

        return IntStream.range(1, duration + 1)
                .mapToObj(i ->createItineraryDay(draftData, i, index))
                .toList();
    }

    private UserItineraryDayDTO createItineraryDay(DraftData draftData, int dayNumber, AtomicInteger index) {
        List<Country> countries = draftData.getCountries();
        List<City> cities = draftData.getCities();
        List<Activity> activities = draftData.getActivities();
        List<HotelDto> hotels = fetchHotels(draftData);
        List<City> visitedCities = buildFlightCities(draftData);
        int totalPeople = draftData.getDraft().getNumberAdults() + draftData.getDraft().getNumberKids();

        UserItineraryDayDTO day = initDay(dayNumber, draftData);
        assignDayData(day, draftData, countries, cities, activities, hotels, visitedCities, totalPeople, index);

        return day;
    }


    private List<HotelDto> fetchHotels(DraftData draftData){
        return hotelAssigner.getHotels(draftData.getDraft().getHotelStanding(), draftData.getCities()).block();
    }

    private LinkedList<City> buildFlightCities(DraftData draftData){
        LinkedList<City> visitedCities = new LinkedList<>(draftData.getCities());
        City departureCity = cityDao.findCityByName(draftData.getDraft().getDepartureCity());

        if (!visitedCities.getFirst().getName().equals(departureCity.getName())) {
            visitedCities.addFirst(departureCity);
        }
        if (!visitedCities.getLast().getName().equals(departureCity.getName())) {
            visitedCities.addLast(departureCity);
        }

        return  visitedCities;
    }

    private UserItineraryDayDTO initDay(int dayNumber, DraftData draftData) {
        UserItineraryDayDTO day = new UserItineraryDayDTO();
        day.setDayNumber(dayNumber);
        LocalDate date = dayAssigner.assignDate(draftData.getDraft().getStartDate(), dayNumber);
        day.setDate(date);
        day.setDayOff(dayAssigner.isDayOff(day));
        return day;
    }

    private void assignDayData(
            UserItineraryDayDTO day,
            DraftData draftData,
            List<Country> countries,
            List<City> cities,
            List<Activity> activities,
            List<HotelDto> hotels,
            List<City> visitedCities,
            int totalPeople,
            AtomicInteger index
    ) {
        day.setCountryName(locationAssigner.assignCountry(day, countries));
        day.setCityName(locationAssigner.assignCity(day, cities, draftData.getDraft().getDuration()));
        day.setActivity(activityAssigner.assignActivity(day, activities, index));
        day.setHotel(hotelAssigner.assignHotel(day, hotels));
        day.setFlightItineraryDTO(flightAssigner.assignFlight(day, visitedCities, totalPeople).block());
    }


}
