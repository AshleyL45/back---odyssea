package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ActivityDao;
import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.mainTables.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.CityNotFound;
import com.example.odyssea.utils.PriceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.*;

@Service
public class ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    private static final String GOOGLE_API_TYPE = "tourist_attraction";
    private static final LocalTime DEFAULT_DURATION = LocalTime.of(1, 0);

    @Value("${GOOGLE_PLACES_API_KEY}")
    private String googlePlacesApiKey;

    private final ActivityDao activityDao;
    private final CityDao cityDao;

    public ActivityService(ActivityDao activityDao, CityDao cityDao) {
        this.activityDao = activityDao;
        this.cityDao = cityDao;
    }

    public List<Activity> getAllActivities() {
        return activityDao.findAll();
    }

    public Activity getActivity(int id) {
        return activityDao.findById(id).orElseThrow(() -> new ActivityNotFound("Activity not found with id: " + id));
    }

    public void createActivity(ActivityDto activityDto, int cityId) {
        checkCityExists(cityId);
        Activity activity = activityDto.toActivity(cityId);
        activityDao.save(activity);
        LOGGER.info("Created activity '{}' in city id {}", activity.getName(), cityId);
    }

    public boolean updateActivity(int id, Activity activity) {
        if (!activityDao.existsById(id)) {
            throw new ActivityNotFound("Activity not found with id: " + id);
        }
        activity.setId(id);
        activityDao.update(activity);
        return true;
    }

    public boolean deleteActivity(int id) {
        if (!activityDao.existsById(id)) {
            throw new ActivityNotFound("Activity not found with id: " + id);
        }
        activityDao.deleteById(id);
        return true;
    }

    public List<Activity> getTop5ActivitiesByCityId(int cityId) {
        checkCityExists(cityId);
        return activityDao.findTop5ByCityId(cityId);
    }

    public void importActivitiesFromGooglePlaces(int cityId, int radius) {
        City city = cityDao.findById(cityId)
                .orElseThrow(() -> new CityNotFound("City not found with id: " + cityId));

        double latitude = city.getLatitude();
        double longitude = city.getLongitude();

        String url = String.format(Locale.US,
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s",
                latitude, longitude, radius, GOOGLE_API_TYPE, googlePlacesApiKey);

        LOGGER.info("Calling Google Places API with URL: {}", url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String status = root.path("status").asText();

                if (!"OK".equals(status)) {
                    LOGGER.error("Google Places API returned status: {}", status);
                    return;
                }

                JsonNode results = root.path("results");
                int currentCount = activityDao.findTop5ByCityId(cityId).size();

                for (int i = 0; i < results.size() && currentCount < 5; i++) {
                    JsonNode resultNode = results.get(i);
                    String name = resultNode.path("name").asText();
                    String vicinity = resultNode.has("vicinity") ? resultNode.path("vicinity").asText() : "";
                    String description = vicinity.isEmpty() ? "No description available" : vicinity;

                    if (!activityDao.activityExists(cityId, name)) {
                        Activity activity = new Activity(0, cityId, name, "Tourist Attraction", "Low",
                                DEFAULT_DURATION, description, PriceUtils.generatePriceInTens());
                        activityDao.save(activity);
                        currentCount++;
                        LOGGER.info("Imported activity '{}' into city {}", name, cityId);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error parsing Google Places API response", e);
            }
        } else {
            LOGGER.error("Google Places API call failed with status: {}", response.getStatusCode());
        }
    }

    public void checkCityExists(int cityId) {
        boolean exists = cityDao.findById(cityId).isPresent();
        if (!exists) {
            throw new CityNotFound("City with id " + cityId + " not found.");
        }
    }
}