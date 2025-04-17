package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DayAssigner {

    public LocalDate assignDate(LocalDate startDate, int dayNumber){
        return startDate.plusDays(dayNumber);
    }

    public boolean isDayOff(UserItineraryDayDTO day, int duration){
        int dayNumber = day.getDayNumber();

        if(dayNumber == 0 || dayNumber == duration){
            return true;
        }

        return (dayNumber - 1) % 4 == 0;
    }
}
