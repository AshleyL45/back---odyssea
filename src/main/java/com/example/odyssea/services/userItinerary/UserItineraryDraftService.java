package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.ActivityDao;
import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.daos.userItinerary.drafts.*;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.enums.TripDuration;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserItineraryDraftService {
    private final UserItineraryDraftDao userItineraryDraftDao;
    private final CurrentUserService currentUserService;
    private final CountryDao countryDao;
    private final CityDao cityDao;
    private final ActivityDao activityDao;
    private final DraftCountriesDao draftCountriesDao;
    private final DraftCitiesDao draftCitiesDao;
    private final DraftActivitiesDao draftActivitiesDao;
    private final DraftOptionsDao draftOptionsDao;
    private final OptionDao optionDao;


    public UserItineraryDraftService(UserItineraryDraftDao userItineraryDraftDao, CurrentUserService currentUserService, CountryDao countryDao, CityDao cityDao, DraftCountriesDao draftCountriesDao, DraftCitiesDao draftCitiesDao, DraftActivitiesDao draftActivitiesDao, ActivityDao activityDao, OptionDao optionDao, DraftOptionsDao draftOptionsDao) {
        this.userItineraryDraftDao = userItineraryDraftDao;
        this.currentUserService = currentUserService;
        this.countryDao = countryDao;
        this.cityDao = cityDao;
        this.draftCountriesDao = draftCountriesDao;
        this.draftCitiesDao = draftCitiesDao;
        this.draftActivitiesDao = draftActivitiesDao;
        this.activityDao = activityDao;
        this.optionDao = optionDao;
        this.draftOptionsDao = draftOptionsDao;
    }

    private Integer getUserId() {
        return currentUserService.getCurrentUserId();
    }

    public void validateFirstStep (int duration) {
        if(!(duration == 9 || duration == 17 || duration == 25 || duration == 33)){
            throw new ValidationException("The duration must be of 9, 17, 25 or 33 days.");
        }

        Integer userId = getUserId();
        userItineraryDraftDao.saveFirstStep(userId, duration);
    }

    public void validateStartDate(String date) {
        if (date == null) {
            throw new ValidationException("Date cannot be null.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate validDate = LocalDate.parse(date, formatter);

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(7);

        if (validDate.isBefore(today) || validDate.isBefore(maxDate)) {
            throw new ValidationException("Date must be in the future at least 7 days after today.");
        }

        Integer userId = getUserId();
        userItineraryDraftDao.saveDate(userId, validDate);
    }

    public void validateDepartureCity(String departureCity){
        if (departureCity == null || departureCity.isEmpty()) {
            throw new ValidationException("Departure city cannot be null.");
        }
        cityDao.findCityByName(departureCity);
        Integer userId = getUserId();
        userItineraryDraftDao.saveDepartureCity(userId, departureCity);
    }

    public void validateCountries(List<Integer> countriesIds){
        Integer userId = getUserId();
        Integer duration = userItineraryDraftDao.getDurationByUserId(userId);

        TripDuration tripDuration = TripDuration.fromDays(duration);
        int expectedCountries = tripDuration.getExpectedCountries();

        if (countriesIds.size() != expectedCountries) {
            throw new ValidationException("The number of countries selected does not match the required number for the duration.");
        }

        for(Integer id : countriesIds){
            countryDao.findById(id);
        }


        draftCountriesDao.saveCountries(userId, countriesIds);
    }

    public void validateCities(List<Integer> citiesIds){
        Integer userId = getUserId();
        Integer duration = userItineraryDraftDao.getDurationByUserId(userId);
        List<Integer> countryIds = draftCountriesDao.getCountriesByDraftId(userId);

        TripDuration tripDuration = TripDuration.fromDays(duration);
        int expectedCities = tripDuration.getExpectedCities();
        if(citiesIds == null || citiesIds.size() != expectedCities){
            throw new ValidationException("You must select exactly " + expectedCities + " cities for your trip duration.");
        }


        for (Integer id : citiesIds) {
            City city = cityDao.findById(id)
                    .orElseThrow(() -> new ValidationException("City with id " + id + " not found."));
            if (!countryIds.contains(city.getCountryId())) {
                throw new ValidationException("City " + city.getName() + " does not belong to selected countries.");
            }
        }

        draftCitiesDao.saveCities(userId, citiesIds);
    }

    public void validateActivities(List<Integer> activitiesIds){
        Integer userId = getUserId();
        Integer duration = userItineraryDraftDao.getDurationByUserId(userId);
        List<Integer> cityIds = draftCitiesDao.getCitiesByDraftId(userId);

        TripDuration tripDuration = TripDuration.fromDays(duration);
        int expectedActivities = tripDuration.getExpectedActivities();
        if(activitiesIds == null || activitiesIds.size() != expectedActivities){
            throw new ValidationException("You must select exactly " + expectedActivities + " activities for your trip duration.");
        }

        for (Integer id : activitiesIds) {
            Activity activity = activityDao.findById(id)
                    .orElseThrow(() -> new ValidationException("Activity with id " + id + " not found."));
            if (!cityIds.contains(activity.getCityId())) {
                throw new ValidationException("Activity " + activity.getName() + " does not belong to selected cities.");
            }
        }

        draftActivitiesDao.saveActivities(userId, activitiesIds);
    }

    public void validateHotelStanding(Integer hotelStanding){
        Integer userId = getUserId();
        if(hotelStanding != 4 && hotelStanding != 5){
            throw new ValidationException("Hotel standing must be between 4 and 5");
        }
        userItineraryDraftDao.saveHotelStanding(userId, hotelStanding);
    }

    public void validateTravelersNumber(Integer numberAdults, Integer numberKids){
        Integer userId = getUserId();
        if(numberAdults <= 0){
            throw new ValidationException("There must be at least an adult");
        } else if(numberAdults + numberKids > 15){
            throw new ValidationException("There must there cannot be more then 15 people");
        }
        userItineraryDraftDao.saveTravelsNumber(userId, numberAdults, numberKids);
    }

    public void validateOptions(List<Integer> optionsIds){
        if(optionsIds.isEmpty()){
            return;
        }

        Integer userId = getUserId();
        for(Integer id : optionsIds){
            optionDao.findById(id);
        }

        draftOptionsDao.saveOptions(userId, optionsIds);
    }
}
