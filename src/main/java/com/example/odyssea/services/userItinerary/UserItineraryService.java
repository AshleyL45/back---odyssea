package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.daos.flight.PlaneRideDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryOptionDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.daos.userItinerary.drafts.UserItineraryDraftDao;
import com.example.odyssea.dtos.flight.FlightItineraryDTO;
import com.example.odyssea.dtos.flight.FlightSegmentDTO;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.dtos.userItinerary.*;
import com.example.odyssea.entities.mainTables.*;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.entities.userItinerary.drafts.UserItineraryDraft;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import com.example.odyssea.services.mainTables.HotelService;
import com.example.odyssea.services.flight.PlaneRideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class UserItineraryService {
    private CityDao cityDao;
    private UserItineraryDao userItineraryDao;
    private UserItineraryStepDao userItineraryStepDao;
    private UserDailyPlanService userDailyPlanService;
    private UserItineraryDraftService userItineraryDraftService;
    private UserItineraryOptionDao userItineraryOptionDao;
    private CurrentUserService currentUserService;


    public UserItineraryService() {

    }

    @Autowired
    public UserItineraryService(CityDao cityDao, UserItineraryDao userItineraryDao, UserItineraryStepDao userItineraryStepDao, UserDailyPlanService userDailyPlanService, UserItineraryDraftService userItineraryDraftService, UserItineraryOptionDao userItineraryOptionDao, CurrentUserService currentUserService) {
        this.cityDao = cityDao;
        this.userItineraryDao = userItineraryDao;
        this.userItineraryStepDao = userItineraryStepDao;
        this.userDailyPlanService = userDailyPlanService;
        this.userItineraryDraftService = userItineraryDraftService;
        this.userItineraryOptionDao = userItineraryOptionDao;
        this.currentUserService = currentUserService;
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }

   public UserItineraryDTO generateItinerary (){
        UserItineraryDTO personalizedTrip = new UserItineraryDTO();
        Integer userId = currentUserService.getCurrentUserId();
        DraftData draftData = userItineraryDraftService.loadAllDraftData(userId);
        List<UserItineraryDayDTO> days = userDailyPlanService.generateEachDay(draftData);
        LocalDate endDate = draftData.getDraft().getStartDate().plusDays(draftData.getDraft().getDuration());
        List<Option> options = draftData.getOptions();
        //BigDecimal price = calculateTotal(draftData.getCountries().)

        personalizedTrip.setUserId(userId);
        personalizedTrip.setItineraryDays(days);
        personalizedTrip.setStartDate(draftData.getDraft().getStartDate());
        personalizedTrip.setEndDate(endDate);
        personalizedTrip.setTotalDuration(draftData.getDraft().getDuration());
        personalizedTrip.setDepartureCity(draftData.getDraft().getDepartureCity());
        personalizedTrip.setNumberOfAdults(draftData.getDraft().getNumberAdults());
        personalizedTrip.setNumberOfKids(draftData.getDraft().getNumberKids());
        personalizedTrip.setItineraryName(null);
        personalizedTrip.setOptions(options);
        personalizedTrip.setStartingPrice(BigDecimal.ZERO);

        return personalizedTrip;
   }

    // Retourner la liste de tous les itinéraires d'un utilisateur
    public List<UserItinerary> getAllUserItineraries(int userId) {
        List<UserItinerary> itineraries = userItineraryDao.findAllUserItineraries(userId);

        for (UserItinerary itinerary : itineraries) {
            String cityName = cityDao.findByIATACode(itinerary.getDepartureCity()).getName();
            itinerary.setDepartureCity(cityName);
        }

        return itineraries;
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

    public boolean updateItineraryName(int id, String newItineraryName){
        return userItineraryDao.updateUserItineraryName(id, newItineraryName);
    }

    // Retourner un itinéraire
   /* public UserItineraryDTO getAUserItineraryById(int userItineraryId){
        UserItinerary userItinerary = userItineraryDao.findById(userItineraryId);
        System.out.println("Days of itinerary : " + toUserItineraryDTO(userItinerary).getItineraryName());
        return toUserItineraryDTO(userItinerary);
    }*/

    /*public UserItineraryDTO toUserItineraryDTO (UserItinerary userItinerary){
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

    }*/

}