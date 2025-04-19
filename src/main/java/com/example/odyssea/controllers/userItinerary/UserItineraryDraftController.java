package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.draft.*;
import com.example.odyssea.exceptions.ErrorResponse;
import com.example.odyssea.exceptions.SuccessResponse;
import com.example.odyssea.services.userItinerary.UserItineraryDraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generate")
@Tag(name = "Questionnaire for personalized trip", description = "Steps to create a personalized trip.")
public class UserItineraryDraftController {

    private final UserItineraryDraftService userItineraryDraftService;

    public UserItineraryDraftController(UserItineraryDraftService userItineraryDraftService) {
        this.userItineraryDraftService = userItineraryDraftService;
    }

    @Operation(
            summary = "Step 1 - Duration and departure date",
            description = "Validates the duration and departure date of the user"
    )

    @PostMapping("/step1")
    public ResponseEntity<SuccessResponse> handleDurationAndDate(@RequestBody DurationDate request){
        Integer duration = request.getDuration();
        String date = request.getStartDate();
        userItineraryDraftService.validateDuration(duration);
        userItineraryDraftService.validateStartDate(date);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 2 - Departure city",
            description = "Validates the departure city"
    )

    @PostMapping("/step2")
    public ResponseEntity<SuccessResponse> handleDepartureCity(@RequestBody DepartureCity departureCity){
        String city = departureCity.getDepartureCity();
        userItineraryDraftService.validateDepartureCity(city);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 3 - Travelers numbers",
            description = "Validates the number of travelers"
    )

    @PostMapping("/step3")
    public ResponseEntity<SuccessResponse> handleTravelersNumber (@RequestBody NumberTravelers request){
        Integer numberAdults = request.getNumberAdults();
        Integer numberKids = request.getNumberKids();
        userItineraryDraftService.validateTravelersNumber(numberAdults, numberKids);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 4 - Countries choice",
            description = "Validates the number of countries chosen"
    )

    @PostMapping("/step4")
    public ResponseEntity<SuccessResponse> handleCountries(@RequestBody CountryList request){
        List<Integer> countries = request.getCountries();
        userItineraryDraftService.validateCountries(countries);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 5 - Cities choice",
            description = "Validates the number of cities chosen"
    )

    @PostMapping("/step5")
    public ResponseEntity<SuccessResponse> handleCities(@RequestBody CityList request){
        List<Integer> cities = request.getCities();
        userItineraryDraftService.validateCities(cities);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 6 - Activities choice",
            description = "Validates the number of activities chosen"
    )

    @PostMapping("/step6")
    public ResponseEntity<SuccessResponse> handleActivities(@RequestBody ActivityList request){
        List<Integer> activities = request.getActivities();
        userItineraryDraftService.validateActivities(activities);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 7 - Hotel standing choice",
            description = "Validates the hotel standing"
    )

    @PostMapping("/step7")
    public ResponseEntity<SuccessResponse> handleHotelStanding (@RequestBody HotelStanding request){
        Integer hotelStanding = request.getHotelStanding();
        userItineraryDraftService.validateHotelStanding(hotelStanding);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


    @Operation(
            summary = "Step 8 - Options choice",
            description = "Check if the options are valid"
    )

    @PostMapping("/step8")
    public ResponseEntity<SuccessResponse> handleOptions(@RequestBody OptionList request){
        List<Integer> options = request.getOptions();
        userItineraryDraftService.validateOptions(options);
        return ResponseEntity.ok(new SuccessResponse(true));
    }


}
