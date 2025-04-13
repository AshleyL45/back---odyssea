package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.services.userItinerary.UserItineraryDraftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generate")
public class UserItineraryDraftController {
    private final UserItineraryDraftService userItineraryDraftService;

    public UserItineraryDraftController(UserItineraryDraftService userItineraryDraftService) {
        this.userItineraryDraftService = userItineraryDraftService;
    }

    @PostMapping("/step1")
    public ResponseEntity<Map<String, Boolean>> handleFirstStep(@RequestBody Map<String, Integer> durationRequest){
        Integer duration = durationRequest.get("duration");
        userItineraryDraftService.validateFirstStep(duration);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/step2")
    public ResponseEntity<Map<String, Boolean>> handleStartDate(@RequestBody Map<String, String> dateRequest){
        String date = dateRequest.get("startDate");
        userItineraryDraftService.validateStartDate(date);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/step3")
    public ResponseEntity<Map<String, Boolean>> handleDepartureCity(@RequestBody Map<String, String> departureCity){
        String city = departureCity.get("departureCity");
        userItineraryDraftService.validateDepartureCity(city);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/step4")
    public ResponseEntity<Map<String, Boolean>> handleCountries(@RequestBody Map<String, List<Integer>> request){
        List<Integer> countries = request.get("countries");
        userItineraryDraftService.validateCountries(countries);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
