package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.draft.*;
import com.example.odyssea.services.userItinerary.UserItineraryDraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<ApiResponse<Void>> handleDurationAndDate(@RequestBody DurationDate request){
        Integer duration = request.getDuration();
        String date = request.getStartDate();
        userItineraryDraftService.validateDuration(duration);
        userItineraryDraftService.validateStartDate(date);
        return ResponseEntity.ok(ApiResponse.success("Duration and date validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 2 - Departure city",
            description = "Validates the departure city"
    )

    @PostMapping("/step2")
    public ResponseEntity<ApiResponse<Void>> handleDepartureCity(@RequestBody DepartureCity departureCity){
        String city = departureCity.getDepartureCity();
        userItineraryDraftService.validateDepartureCity(city);
        return ResponseEntity.ok(ApiResponse.success("Departure city validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 3 - Travelers numbers",
            description = "Validates the number of travelers"
    )

    @PostMapping("/step3")
    public ResponseEntity<ApiResponse<Void>> handleTravelersNumber (@RequestBody NumberTravelers request){
        Integer numberAdults = request.getNumberAdults();
        Integer numberKids = request.getNumberKids();
        userItineraryDraftService.validateTravelersNumber(numberAdults, numberKids);
        return ResponseEntity.ok(ApiResponse.success("Number of travelers validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 4 - Countries choice",
            description = "Validates the number of countries chosen"
    )

    @PostMapping("/step4")
    public ResponseEntity<ApiResponse<Void>> handleCountries(@RequestBody CountryList request){
        List<Integer> countries = request.getCountries();
        userItineraryDraftService.validateCountries(countries);
        return ResponseEntity.ok(ApiResponse.success("Countries validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 5 - Cities choice",
            description = "Validates the number of cities chosen"
    )

    @PostMapping("/step5")
    public ResponseEntity<ApiResponse<Void>> handleCities(@RequestBody CityList request){
        List<Integer> cities = request.getCities();
        userItineraryDraftService.validateCities(cities);
        return ResponseEntity.ok(ApiResponse.success("Cities validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 6 - Activities choice",
            description = "Validates the number of activities chosen"
    )

    @PostMapping("/step6")
    public ResponseEntity<ApiResponse<Void>> handleActivities(@RequestBody ActivityList request){
        List<Integer> activities = request.getActivities();
        userItineraryDraftService.validateActivities(activities);
        return ResponseEntity.ok(ApiResponse.success("Activities validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 7 - Hotel standing choice",
            description = "Validates the hotel standing"
    )

    @PostMapping("/step7")
    public ResponseEntity<ApiResponse<Void>> handleHotelStanding (@RequestBody HotelStanding request){
        Integer hotelStanding = request.getHotelStanding();
        userItineraryDraftService.validateHotelStanding(hotelStanding);
        return ResponseEntity.ok(ApiResponse.success("Hotel standing validated.", HttpStatus.OK));
    }


    @Operation(
            summary = "Step 8 - Options choice",
            description = "Check if the options are valid"
    )

    @PostMapping("/step8")
    public ResponseEntity<ApiResponse<Void>> handleOptions(@RequestBody OptionList request){
        List<Integer> options = request.getOptions();
        userItineraryDraftService.validateOptions(options);
        return ResponseEntity.ok(ApiResponse.success("Step 8: Options validated.", HttpStatus.OK));
    }


}
