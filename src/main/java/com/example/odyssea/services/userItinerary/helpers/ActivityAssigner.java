package com.example.odyssea.services.userItinerary.helpers;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.userItinerary.UserItineraryDayDTO;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActivityAssigner {
    public final CityDao cityDao;
    private static final Logger log = LoggerFactory.getLogger(ActivityAssigner.class);

    public ActivityAssigner(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public Activity assignActivity(UserItineraryDayDTO day, List<Activity> activityList, AtomicInteger activityIndex) {

        if (day.isDayOff()) {
            return null;
        }

        if(activityList.isEmpty()){
            throw new ActivityNotFound("Activity list is empty.");
        }

        if (activityIndex.get() >= activityList.size()) {
            return null;
        }

        City cityDay = cityDao.findCityByName(day.getCityName());

        Activity selectedActivity = activityList.get(activityIndex.getAndIncrement());

        if (!(selectedActivity.getCityId() == (cityDay.getId()))) {
            log.warn("City mismatch: Activity {} (city {}) assigned to day in city {}",
                    selectedActivity.getId(), selectedActivity.getCityId(), cityDay.getId());

        }
        return selectedActivity;
    }

}
