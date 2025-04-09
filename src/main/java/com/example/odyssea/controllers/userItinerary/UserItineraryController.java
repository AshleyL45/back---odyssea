package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.userItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.userItinerary.UserRequestDTO;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.exceptions.ErrorResponse;
import com.example.odyssea.services.userItinerary.UserItineraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userItinerary")
public class UserItineraryController {
    private final UserItineraryService userItineraryService;

    public UserItineraryController(UserItineraryService userItineraryService) {
        this.userItineraryService = userItineraryService;
    }

    // Avoir tous les itinéraires personnalisés d'un utilisateur
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<UserItinerary>> getAllUserItineraries(@PathVariable int userId){
        List<UserItinerary> userItineraryDTOs = userItineraryService.getAllUserItineraries(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userItineraryDTOs);
    }

    // Avoir un itinéraire en particulier
    @GetMapping("/{userItineraryId}")
    public ResponseEntity<UserItineraryDTO> getAnItinerary(@PathVariable int userItineraryId){
        UserItineraryDTO userItinerary = userItineraryService.getAUserItineraryById(userItineraryId);
        return ResponseEntity.status(HttpStatus.OK).body(userItinerary);
    }

    @PostMapping("/itineraryName/{id}")
    public ResponseEntity<String> updateItineraryName(@PathVariable int id, @RequestBody Map<String, String> itineraryName){
        String newItineraryName = itineraryName.get("itineraryName");
        boolean isUpdated = userItineraryService.updateItineraryName(id, newItineraryName);
        if(isUpdated){
            return ResponseEntity.ok("The name of your personalized itinerary was successfully saved.");
        } else {
            return ResponseEntity.badRequest().body("An error occurred. Please try again.");
        }
    }

    @PostMapping("/generate/step1")
    public ResponseEntity<?> handleStep1( @RequestBody Map<String, Integer> durationRequest){
        Integer duration = durationRequest.get("duration");
        userItineraryService.validateStep1(duration);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/generate/step2")
    public ResponseEntity<?> handleStep2( @RequestBody Map<String, String> dateRequest){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = dateRequest.get("startDate");
        try {
            LocalDate validDate = LocalDate.parse(date, formatter);
            boolean isValidated = userItineraryService.validateStep2(validDate);
            if(isValidated){
                return ResponseEntity.ok(Map.of("success", true));
            }
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Validation Error",
                    e.getMessage()
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/generate/step3")
    public ResponseEntity<?> handleStep3( @RequestBody Map<String, List<String>> countriesRequest){
        List<String> countries = countriesRequest.get("countries");
        userItineraryService.validateStep3(countries);

        return ResponseEntity.ok(Map.of("success", true));
    }


    // Générer un itinéraire
    @PostMapping("/generate")
        public ResponseEntity<UserItineraryDTO> generateItinerary(@RequestBody UserRequestDTO userPreferences) throws Exception {
        UserItineraryDTO userItinerary = userItineraryService.generateUserItinerary(userPreferences);
        return ResponseEntity.status(HttpStatus.CREATED).body(userItinerary);
    }

}
