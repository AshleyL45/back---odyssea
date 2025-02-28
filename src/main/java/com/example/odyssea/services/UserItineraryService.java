package com.example.odyssea.services;

import com.example.odyssea.dtos.UserItinerary.UserItineraryDTO;
import com.example.odyssea.dtos.UserItinerary.UserItineraryDayDTO;
import com.example.odyssea.dtos.UserItinerary.UserPreferencesDTO;
import org.springframework.stereotype.Service;


@Service
public class UserItineraryService {
    private UserItineraryDTO userItineraryDTO;
    private UserItineraryDayDTO userItineraryDayDTO;


    public UserItineraryService() {
    }

    public UserItineraryService(UserItineraryDTO userItineraryDTO, UserItineraryDayDTO userItineraryDayDTO) {
        this.userItineraryDTO = userItineraryDTO;
        this.userItineraryDayDTO = userItineraryDayDTO;
    }

    public UserItineraryDTO generateUserItinerary(UserPreferencesDTO userPreferences) {
        UserItineraryDTO userItinerary = new UserItineraryDTO();
        return userItinerary;
    }
}
