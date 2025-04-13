package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.ActivityDao;
import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.daos.userItinerary.drafts.*;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserItineraryDraftService {
    private UserItineraryDraftDao userItineraryDraftDao;
    private CurrentUserService currentUserService;
    private CountryDao countryDao;
    private CityDao cityDao;
    private ActivityDao activityDao;
    private DraftCountriesDao draftCountriesDao;
    private DraftCitiesDao draftCitiesDao;
    private DraftActivitiesDao draftActivitiesDao;
    private DraftOptionsDao draftOptionsDao;
    private OptionDao optionDao;


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

    private int getExpectedCountryCount(int duration) {
        return switch (duration) {
            case 9 -> 1;
            case 17 -> 2;
            case 25 -> 3;
            case 33 -> 4;
            default -> throw new ValidationException("Invalid duration. The duration must be one of 9, 17, 25, or 33.");
        };
    }

    private int getExpectedCityCount(int duration) {
        return switch (duration) {
            case 9 -> 2;
            case 17 -> 4;
            case 25 -> 6;
            case 33 -> 8;
            default -> throw new ValidationException("Invalid duration. The duration must be one of 9, 17, 25, or 33.");
        };
    }

    private int getExpectedActivityCount(int duration) {
        return switch (duration) {
            case 9 -> 6;
            case 17 -> 12;
            case 25 -> 18;
            case 33 -> 24;
            default -> throw new ValidationException("Invalid duration. The duration must be one of 9, 17, 25, or 33.");
        };
    }

    public void validateFirstStep (int duration) {
        if(!(duration == 9 || duration == 17 || duration == 25 || duration == 33)){
            throw new ValidationException("The duration must be of 9, 17, 25 or 33 days.");
        }

        Integer userId = currentUserService.getCurrentUserId();
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

        Integer userId = currentUserService.getCurrentUserId();
        userItineraryDraftDao.saveDate(userId, validDate);
    }

    public void validateDepartureCity(String departureCity){
        if (departureCity == null || departureCity.isEmpty()) {
            throw new ValidationException("Departure city cannot be null.");
        }
        cityDao.findCityByName(departureCity);
        Integer userId = currentUserService.getCurrentUserId();
        userItineraryDraftDao.saveDepartureCity(userId, departureCity);
    }

    public void validateCountries(List<Integer> countriesIds){
        Integer userId = currentUserService.getCurrentUserId();
        Integer duration = userItineraryDraftDao.getDurationByUserId(userId);

        int expectedNumberOfCountries = getExpectedCountryCount(duration);

        if (countriesIds.size() != expectedNumberOfCountries) {
            throw new ValidationException("The number of countries selected does not match the required number for the duration.");
        }

        for(Integer id : countriesIds){
            countryDao.findById(id);
        }


        draftCountriesDao.saveCountries(userId, countriesIds);
    }

    public void validateCities(List<Integer> citiesIds){
        Integer userId = currentUserService.getCurrentUserId();
        Integer duration = userItineraryDraftDao.getDurationByUserId(userId);
        List<Integer> countryIds = draftCountriesDao.getCountriesByDraftId(userId);

        int expectedNumberOfCities = getExpectedCityCount(duration);
        if(citiesIds == null || citiesIds.size() != expectedNumberOfCities){
            throw new ValidationException("You must select exactly " + expectedNumberOfCities + " cities for your trip duration.");
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
        Integer userId = currentUserService.getCurrentUserId();
        Integer duration = userItineraryDraftDao.getDurationByUserId(userId);
        List<Integer> cityIds = draftCitiesDao.getCitiesByDraftId(userId);

        int expectedNumberOfActivities = getExpectedActivityCount(duration);
        if(activitiesIds == null || activitiesIds.size() != expectedNumberOfActivities){
            throw new ValidationException("You must select exactly " + expectedNumberOfActivities + " activities for your trip duration.");
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
        Integer userId = currentUserService.getCurrentUserId();
        if(hotelStanding != 4 && hotelStanding != 5){
            throw new ValidationException("Hotel standing must be between 4 and 5");
        }
        userItineraryDraftDao.saveHotelStanding(userId, hotelStanding);
    }

    public void validateTravelersNumber(Integer numberAdults, Integer numberKids){
        Integer userId = currentUserService.getCurrentUserId();
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

        Integer userId = currentUserService.getCurrentUserId();
        for(Integer id : optionsIds){
            optionDao.findById(id);
        }

        draftOptionsDao.saveOptions(userId, optionsIds);
    }
}
