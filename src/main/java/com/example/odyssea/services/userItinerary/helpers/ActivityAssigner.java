package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActivityAssigner {

    public Activity assignActivity(UserItineraryDayDTO day, List<Activity> activityList, AtomicInteger activityIndex) {
        StopWatch watch = new StopWatch();
        watch.start("Assigning activity for a day");
        if (day.isDayOff()) {
            return null;
        }

        if(activityList.isEmpty()){
            throw new ValidationException("Activity list is empty. Please choose activities.");
        }

        if (activityIndex.get() >= activityList.size()) {
            return null;
        }

        watch.stop();
        System.out.println(watch.prettyPrint());
        return activityList.get(activityIndex.getAndIncrement());
    }
}
