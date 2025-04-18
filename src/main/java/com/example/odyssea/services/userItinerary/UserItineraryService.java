package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryOptionDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.userItinerary.*;
import com.example.odyssea.entities.mainTables.*;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


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

    @Transactional
    public UserItinerary saveUserItinerary(UserItineraryDTO userItineraryDTO) {
        UserItinerary userItinerary = new UserItinerary();
        userItinerary.setUserId(userItineraryDTO.getUserId());
        userItinerary.setStartDate(userItineraryDTO.getStartDate());
        userItinerary.setEndDate(userItineraryDTO.getEndDate());
        userItinerary.setTotalDuration(userItineraryDTO.getTotalDuration());
        userItinerary.setDepartureCity(userItineraryDTO.getDepartureCity());
        userItinerary.setStartingPrice(userItineraryDTO.getStartingPrice());
        userItinerary.setItineraryName(userItineraryDTO.getItineraryName());
        userItinerary.setNumberOfAdults(userItineraryDTO.getNumberOfAdults());
        userItinerary.setNumberOfKids(userItineraryDTO.getNumberOfKids());

        UserItinerary savedItinerary = userItineraryDao.save(userItinerary);

        if (userItineraryDTO.getOptions() != null || !userItineraryDTO.getOptions().isEmpty()) {
            for (Option option : userItineraryDTO.getOptions()) {
                userItineraryOptionDao.save(savedItinerary.getId(), option.getId());
            }
        }

        userDailyPlanService.saveUserDailyPlans(savedItinerary, userItineraryDTO.getItineraryDays());
        return savedItinerary;
    }


    public UserItineraryDTO generateItinerary (){
        UserItineraryDTO personalizedTrip = new UserItineraryDTO();
        Integer userId = currentUserService.getCurrentUserId();
        DraftData draftData = userItineraryDraftService.loadAllDraftData(userId);
        List<UserItineraryDayDTO> days = userDailyPlanService.generateDailyPlan(draftData);
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

        UserItinerary userItinerarySaved = saveUserItinerary(personalizedTrip);
        personalizedTrip.setId(userItinerarySaved.getId());

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
   public UserItineraryDTO getAUserItineraryById(int userItineraryId){
        UserItinerary userItinerary = userItineraryDao.findById(userItineraryId);
        return toUserItineraryDTO(userItinerary);
   }

   @Transactional
    public UserItineraryDTO toUserItineraryDTO (UserItinerary userItinerary){
        LocalDate startDate = userItinerary.getStartDate();
        LocalDate endDate = userItinerary.getEndDate();

        List<UserItineraryStep> daysEntities = userItineraryStepDao.findDailyPlansOfAnItinerary(userItinerary.getId());

        List<UserItineraryDayDTO> days = new ArrayList<>();
        for(UserItineraryStep day : daysEntities){
            days.add(userDailyPlanService.toUserItineraryDay(userItinerary, day));
        }

        List<Option> options = userItineraryOptionDao.findOptionsByUserItineraryId(userItinerary.getId());
        if(options.isEmpty()){
            options = new ArrayList<>();
        }

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

}