package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActivityAssigner {

    public Activity assignActivity(UserItineraryDayDTO day, List<Activity> activityList, AtomicInteger activityIndex) {
        if (day.isDayOff()) {
            return null;
        }

        if(activityList.isEmpty()){
            throw new ValidationException("Activity list is empty. Please choose activities.");
        }

        if (activityIndex.get() >= activityList.size()) {
            return null;
        }

        return activityList.get(activityIndex.getAndIncrement());
    }
}
