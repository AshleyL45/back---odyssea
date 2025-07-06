package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.GlobalExceptionHandler;
import com.example.odyssea.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ActivityAssigner {
    public final CityDao cityDao;
    private static final Logger log = LoggerFactory.getLogger(ActivityAssigner.class);

    public ActivityAssigner(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public Activity assignActivity(UserItineraryDayDTO day, List<Activity> activityList) {
        if (day.isDayOff()) {
            return null;
        }

        City cityDay = cityDao.findCityByName(day.getCityName());

        List<Activity> matchingActivities = activityList.stream()
                .filter(a -> a.getCityId() == cityDay.getId())
                .toList();

        if (matchingActivities.isEmpty()) {
            throw new ActivityNotFound("No matching activity for city " + cityDay.getName());
        }

        Activity selected = matchingActivities.get(new Random().nextInt(matchingActivities.size()));

        activityList.remove(selected);

        return selected;
    }


}
