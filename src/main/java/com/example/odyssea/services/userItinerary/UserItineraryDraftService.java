package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.daos.userItinerary.drafts.DraftCountriesDao;
import com.example.odyssea.daos.userItinerary.drafts.UserItineraryDraftDao;
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
    private DraftCountriesDao draftCountriesDao;


    public UserItineraryDraftService(UserItineraryDraftDao userItineraryDraftDao, CurrentUserService currentUserService, CountryDao countryDao, CityDao cityDao, DraftCountriesDao draftCountriesDao) {
        this.userItineraryDraftDao = userItineraryDraftDao;
        this.currentUserService = currentUserService;
        this.countryDao = countryDao;
        this.cityDao = cityDao;
        this.draftCountriesDao = draftCountriesDao;
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
}
