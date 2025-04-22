package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryOptionDao;
import com.example.odyssea.daos.userItinerary.UserItineraryStepDao;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.dtos.userItinerary.*;
import com.example.odyssea.entities.mainTables.*;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


@Service
public class UserItineraryService {
    private UserItineraryDao userItineraryDao;
    private UserItineraryStepDao userItineraryStepDao;
    private UserDailyPlanService userDailyPlanService;
    private UserItineraryDraftService userItineraryDraftService;
    private UserItineraryOptionDao userItineraryOptionDao;
    private CurrentUserService currentUserService;


    public UserItineraryService() {

    }

    @Autowired
    public UserItineraryService(UserItineraryDao userItineraryDao, UserItineraryStepDao userItineraryStepDao, UserDailyPlanService userDailyPlanService, UserItineraryDraftService userItineraryDraftService, UserItineraryOptionDao userItineraryOptionDao, CurrentUserService currentUserService) {
        this.userItineraryDao = userItineraryDao;
        this.userItineraryStepDao = userItineraryStepDao;
        this.userDailyPlanService = userDailyPlanService;
        this.userItineraryDraftService = userItineraryDraftService;
        this.userItineraryOptionDao = userItineraryOptionDao;
        this.currentUserService = currentUserService;
    }


    public UserItineraryDTO generateItinerary (){
        UserItineraryDTO personalizedTrip = new UserItineraryDTO();
        Integer userId = currentUserService.getCurrentUserId();
        DraftData draftData = userItineraryDraftService.loadAllDraftData(userId);
        List<UserItineraryDayDTO> days = userDailyPlanService.generateDailyPlan(draftData);
        List<HotelDto> hotels = days
                .stream()
                .map(UserItineraryDayDTO::getHotel)
                .toList();

        List<Activity> activities = days
                .stream()
                .filter(Objects::nonNull)
                .map(UserItineraryDayDTO::getActivity)
                .toList();
        LocalDate endDate = draftData.getDraft().getStartDate().plusDays(draftData.getDraft().getDuration());
        List<Option> options = draftData.getOptions();
        BigDecimal price = calculateTotalPrice(draftData.getCountries(), draftData.getOptions(), hotels, activities, draftData.getDraft().getNumberAdults(), draftData.getDraft().getNumberKids());

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
        personalizedTrip.setStartingPrice(price);

        UserItinerary userItinerarySaved = saveUserItinerary(personalizedTrip);
        personalizedTrip.setId(userItinerarySaved.getId());

        return personalizedTrip;
   }

    public List<UserItineraryDTO> getAllUserItineraries() {
        Integer userId = currentUserService.getCurrentUserId();
        List<UserItinerary> foundItineraries = userItineraryDao.findAllUserItineraries(userId);
        List<UserItineraryDTO> userItineraries = new ArrayList<>();


        for (UserItinerary itinerary : foundItineraries) {
            userItineraries.add(toUserItineraryDTO(itinerary));
        }

        return userItineraries;
    }

    public UserItineraryDTO getAUserItineraryById(int userItineraryId){
        UserItinerary userItinerary = userItineraryDao.findById(userItineraryId);
        return toUserItineraryDTO(userItinerary);
   }

    public void updateItineraryName(int id, String newItineraryName){
        userItineraryDao.updateUserItineraryName(id, newItineraryName);
    }

    @Transactional
    public UserItinerary saveUserItinerary(UserItineraryDTO userItineraryDTO) {
        UserItinerary userItinerary = toUserItineraryEntity(userItineraryDTO);
        UserItinerary savedItinerary = userItineraryDao.save(userItinerary);

        if (userItineraryDTO.getOptions() != null && !userItineraryDTO.getOptions().isEmpty()) {
            for (Option option : userItineraryDTO.getOptions()) {
                userItineraryOptionDao.save(savedItinerary.getId(), option.getId());
            }
        }

        userDailyPlanService.saveUserDailyPlans(savedItinerary, userItineraryDTO.getItineraryDays());
        return savedItinerary;
    }

    private BigDecimal calculateTotalPrice(
            List<Country> countries,
            List<Option> options,
            List<HotelDto> hotels,
            List<Activity> activities,
            Integer numberOfAdults,
            Integer numberOfKids
    ) {
        BigDecimal totalPeople = BigDecimal.valueOf(getTotalPeople(numberOfAdults, numberOfKids));

        BigDecimal countrySum = sumCountryPrices(countries);
        BigDecimal optionSum = sumOptionPrices(options);
        BigDecimal hotelSum = sumHotelPrices(hotels);
        BigDecimal activitySum = sumActivityPrices(activities);

        return Stream.of(countrySum, optionSum, hotelSum, activitySum)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(totalPeople);
    }

    private int getTotalPeople(Integer adults, Integer kids) {
        int total = (adults != null ? adults : 0) + (kids != null ? kids : 0);
        if (total == 0) {
            throw new ValidationException("Total number of people isn't valid.");
        }
        return total;
    }

    private BigDecimal sumCountryPrices(List<Country> countries) {
        return countries.stream()
                .map(Country::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumOptionPrices(List<Option> options) {
        return options.stream()
                .filter(Objects::nonNull)
                .map(Option::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumHotelPrices(List<HotelDto> hotels) {
        return hotels.stream()
                .filter(Objects::nonNull)
                .map(HotelDto::getPrice)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumActivityPrices(List<Activity> activities) {
        return activities.stream()
                .filter(Objects::nonNull)
                .map(Activity::getPrice)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private UserItinerary toUserItineraryEntity(UserItineraryDTO dto) {
        UserItinerary entity = new UserItinerary();
        entity.setUserId(dto.getUserId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setTotalDuration(dto.getTotalDuration());
        entity.setDepartureCity(dto.getDepartureCity());
        entity.setStartingPrice(dto.getStartingPrice());
        entity.setItineraryName(dto.getItineraryName());
        entity.setNumberOfAdults(dto.getNumberOfAdults());
        entity.setNumberOfKids(dto.getNumberOfKids());
        return entity;
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