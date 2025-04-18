package com.example.odyssea.controllers.userItinerary;

import com.example.odyssea.dtos.userItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.userItinerary.UserRequestDTO;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import com.example.odyssea.services.userItinerary.UserItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userItinerary")
public class UserItineraryController {
    private final UserItineraryService userItineraryService;

    public UserItineraryController(UserItineraryService userItineraryService) {
        this.userItineraryService = userItineraryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserItineraryDTO>> getAllItineraries(){
        List<UserItineraryDTO> userItineraryDTOs = userItineraryService.getAllUserItineraries();
        return ResponseEntity.status(HttpStatus.OK).body(userItineraryDTOs);
    }

    @GetMapping("/{userItineraryId}")
    public ResponseEntity<UserItineraryDTO> getItineraryById(@PathVariable int userItineraryId){
        UserItineraryDTO userItinerary = userItineraryService.getAUserItineraryById(userItineraryId);
        return ResponseEntity.status(HttpStatus.OK).body(userItinerary);
    }

    @GetMapping("/generate")
        public ResponseEntity<UserItineraryDTO> generateItinerary() {
        UserItineraryDTO userItinerary = userItineraryService.generateItinerary();
        return ResponseEntity.status(HttpStatus.CREATED).body(userItinerary);
    }

    @PatchMapping("/itineraryName/{id}")
    public ResponseEntity<String> updateItineraryName(@PathVariable int id, @RequestBody Map<String, String> itineraryName){
        String newItineraryName = itineraryName.get("itineraryName");
        userItineraryService.updateItineraryName(id, newItineraryName);
        return ResponseEntity.status(HttpStatus.OK).body("Your personalized trip's name has been successfully updated.");
    }

}
