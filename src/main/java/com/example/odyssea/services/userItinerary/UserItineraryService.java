package com.example.odyssea.services.userItinerary;

import com.example.odyssea.daos.userItinerary.UserItineraryDao;
import com.example.odyssea.daos.userItinerary.UserItineraryOptionDao;
import com.example.odyssea.daos.userItinerary.UserDailyPlanDao;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.dtos.userItinerary.*;
import com.example.odyssea.entities.mainTables.*;

import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.entities.userItinerary.UserItineraryStep;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


@Service
public class UserItineraryService {
    private final UserItineraryDao userItineraryDao;
    private final UserDailyPlanDao userDailyPlanDao;
    private final UserDailyPlanService userDailyPlanService;
    private final UserItineraryDraftService userItineraryDraftService;
    private final UserItineraryOptionDao userItineraryOptionDao;
    private final CurrentUserService currentUserService;


    public UserItineraryService(UserItineraryDao userItineraryDao, UserDailyPlanDao userDailyPlanDao, UserDailyPlanService userDailyPlanService, UserItineraryDraftService userItineraryDraftService, UserItineraryOptionDao userItineraryOptionDao, CurrentUserService currentUserService) {
        this.userItineraryDao = userItineraryDao;
        this.userDailyPlanDao = userDailyPlanDao;
        this.userDailyPlanService = userDailyPlanService;
        this.userItineraryDraftService = userItineraryDraftService;
        this.userItineraryOptionDao = userItineraryOptionDao;
        this.currentUserService = currentUserService;
    }

    // Méthodes métier publiques

    public Mono<UserItineraryDTO> generateItineraryAsync() {
        StopWatch watch = new StopWatch();
        watch.start("Generating full itinerary");

        Integer userId = currentUserService.getCurrentUserId();
        DraftData draftData = userItineraryDraftService.loadAllDraftData(userId);

        return userDailyPlanService.generateDailyPlan(draftData)
                .map(days -> {
                    List<HotelDto> hotels = days.stream()
                            .map(UserItineraryDayDTO::getHotel)
                            .toList();

                    List<Activity> activities = days.stream()
                            .filter(Objects::nonNull)
                            .map(UserItineraryDayDTO::getActivity)
                            .toList();

                    BigDecimal price = calculateTotalPrice(
                            draftData.getCountries(),
                            draftData.getOptions(),
                            hotels,
                            activities,
                            draftData.getDraft().getNumberAdults(),
                            draftData.getDraft().getNumberKids());

                    LocalDate endDate = draftData.getDraft().getStartDate().plusDays(draftData.getDraft().getDuration());

                    UserItineraryDTO personalizedTrip = new UserItineraryDTO();
                    personalizedTrip.setUserId(userId);
                    personalizedTrip.setItineraryDays(days);
                    personalizedTrip.setStartDate(draftData.getDraft().getStartDate());
                    personalizedTrip.setEndDate(endDate);
                    personalizedTrip.setTotalDuration(draftData.getDraft().getDuration());
                    personalizedTrip.setDepartureCity(draftData.getDraft().getDepartureCity());
                    personalizedTrip.setNumberOfAdults(draftData.getDraft().getNumberAdults());
                    personalizedTrip.setNumberOfKids(draftData.getDraft().getNumberKids());
                    personalizedTrip.setItineraryName(null);
                    personalizedTrip.setOptions(draftData.getOptions());
                    personalizedTrip.setStartingPrice(price);
                    personalizedTrip.setBookingDate(LocalDate.now());
                    personalizedTrip.setStatus(BookingStatus.PENDING);

                    return personalizedTrip;
                })
                .map(personalizedTrip -> {
                    UserItinerary saved = saveUserItinerary(personalizedTrip);
                    personalizedTrip.setId(saved.getId());

                    watch.stop();
                    System.out.println(watch.prettyPrint());
                    return personalizedTrip;
                });
    }

    public List<UserItineraryDTO> getAllUserItineraries() {
        StopWatch watch = new StopWatch();
        watch.start("Getting all user itineraries");
        Integer userId = currentUserService.getCurrentUserId();
        List<UserItinerary> foundItineraries = userItineraryDao.findAllUserItineraries(userId);

        List<UserItineraryDTO> userItineraries = new ArrayList<>();


        for (UserItinerary itinerary : foundItineraries) {
            userItineraries.add(toUserItineraryDTO(itinerary));
        }

        watch.stop();
        System.out.println(watch.prettyPrint());
        return userItineraries;
    }

    public UserItineraryDTO getAUserItineraryById(int userItineraryId){
        UserItinerary userItinerary = userItineraryDao.findById(userItineraryId);
        return toUserItineraryDTO(userItinerary);
   }

    public void updateItineraryName(int id, String newItineraryName){
        userItineraryDao.updateUserItineraryName(id, newItineraryName);
    }

    // Méthodes de sauvegarde/conversion
    @Transactional
    private UserItinerary saveUserItinerary(UserItineraryDTO userItineraryDTO) {
        UserItinerary userItinerary = toUserItineraryEntity(userItineraryDTO);
        UserItinerary savedItinerary = userItineraryDao.save(userItinerary);

        if (userItineraryDTO.getOptions() != null && !userItineraryDTO.getOptions().isEmpty()) {
            System.out.println("Options size is : " + userItineraryDTO.getOptions().size());
            for (Option option : userItineraryDTO.getOptions()) {
                userItineraryOptionDao.save(savedItinerary.getId(), option.getId());
            }
        }

        userDailyPlanService.saveUserDailyPlans(savedItinerary, userItineraryDTO.getItineraryDays());

        return savedItinerary;
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
        entity.setBookingDate(LocalDate.now());
        entity.setStatus(BookingStatus.PENDING);
        return entity;
    }

    @Transactional
    public UserItineraryDTO toUserItineraryDTO (UserItinerary userItinerary){
        LocalDate startDate = userItinerary.getStartDate();
        LocalDate endDate = userItinerary.getEndDate();

        List<UserItineraryStep> daysEntities = userDailyPlanDao.findDailyPlansOfAnItinerary(userItinerary.getId());

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
                options,
                userItinerary.getBookingDate(),
                userItinerary.getStatus()

        );

    }

    // Méthodes métier internes
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

    private UserItineraryDTO buildUserItineraryDTO(DraftData draftData, List<UserItineraryDayDTO> days, BigDecimal price) {
        LocalDate endDate = draftData.getDraft().getStartDate().plusDays(draftData.getDraft().getDuration());

        UserItineraryDTO dto = new UserItineraryDTO();
        dto.setUserId(currentUserService.getCurrentUserId());
        dto.setItineraryDays(days);
        dto.setStartDate(draftData.getDraft().getStartDate());
        dto.setEndDate(endDate);
        dto.setTotalDuration(draftData.getDraft().getDuration());
        dto.setDepartureCity(draftData.getDraft().getDepartureCity());
        dto.setNumberOfAdults(draftData.getDraft().getNumberAdults());
        dto.setNumberOfKids(draftData.getDraft().getNumberKids());
        dto.setItineraryName(null);
        dto.setOptions(draftData.getOptions());
        dto.setStartingPrice(price);
        dto.setBookingDate(LocalDate.now());
        dto.setStatus(BookingStatus.PENDING);
        return dto;
    }

    // Helpers d'extraction
    private List<HotelDto> extractHotels(List<UserItineraryDayDTO> days) {
        return days.stream()
                .map(UserItineraryDayDTO::getHotel)
                .toList();
    }
    private List<Activity> extractActivities(List<UserItineraryDayDTO> days) {
        return days.stream()
                .filter(Objects::nonNull)
                .map(UserItineraryDayDTO::getActivity)
                .toList();
    }


}