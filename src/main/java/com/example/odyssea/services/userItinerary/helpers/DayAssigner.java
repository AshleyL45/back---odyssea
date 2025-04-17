package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DayAssigner {

    public LocalDate assignDate(LocalDate startDate, int dayNumber){
        return startDate.plusDays(dayNumber);
    }

    public boolean isDayOff(UserItineraryDayDTO day){
        int dayNumber = day.getDayNumber();
        return (dayNumber - 1) % 4 == 0;
    }

}
