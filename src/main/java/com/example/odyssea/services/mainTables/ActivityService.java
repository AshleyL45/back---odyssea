package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ActivityDao;
import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.mainTables.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ActivityNotFound;
import com.example.odyssea.exceptions.CityNotFound;
import com.example.odyssea.exceptions.ExternalServiceException;
import com.example.odyssea.utils.PriceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    private static final String GOOGLE_API_TYPE = "tourist_attraction";
    private static final java.time.LocalTime DEFAULT_DURATION = java.time.LocalTime.of(1, 0);

    private final WebClient webClient;
    private final ActivityDao activityDao;
    private final CityDao cityDao;

    @Value("${GOOGLE_PLACES_API_KEY}")
    private String googlePlacesApiKey;

    public ActivityService(ActivityDao activityDao,
                           CityDao cityDao,
                           WebClient webClient) {
        this.activityDao = activityDao;
        this.cityDao     = cityDao;
        this.webClient   = webClient;                     // ← initialisation manquante
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

    public List<Activity> importAndGetActivities(int cityId, int radius) {
        checkCityExists(cityId);
        List<Activity> activities = getTop5ActivitiesByCityId(cityId);

        if (activities.size() < 5) {
            importActivitiesFromGooglePlaces(cityId, radius);
            activities = getTop5ActivitiesByCityId(cityId);
        }

        if (activities.isEmpty()) {
            throw new ActivityNotFound("No activities found for city ID " + cityId);
        }
        return activities;
    }

    private void importActivitiesFromGooglePlaces(int cityId, int radius) {
        String url = buildPlacesUrl(cityId, radius);
        JsonNode root = fetchFromGooglePlaces(url);
        JsonNode results = root.path("results");

        int count = 0;
        for (JsonNode node : results) {
            if (count >= 5) break;
            String name     = node.path("name").asText(null);
            String vicinity = node.path("vicinity").asText(null);
            if (name == null || vicinity == null) continue;
            if (activityDao.activityExists(cityId, name)) continue;

            Activity act = new Activity(
                    0, cityId, name, "Tourist Attraction",
                    "Low", DEFAULT_DURATION, vicinity,
                    PriceUtils.generatePriceInTens()
            );
            activityDao.save(act);
            count++;
        }
    }

    private JsonNode fetchFromGooglePlaces(String url) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), resp ->
                            Mono.error(new ExternalServiceException(
                                    "Google Places 4xx error for URL " + url)))
                    .onStatus(status -> status.is5xxServerError(), resp ->
                            Mono.error(new ExternalServiceException(
                                    "Google Places 5xx error for URL " + url)))
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(3),
                            Mono.error(new ExternalServiceException("Google Places timeout")))
                    .block();
        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("Failed to call Google Places", e);
        }
    }

    private String buildPlacesUrl(int cityId, int radius) {
        // on sait que checkCityExists a déjà vérifié que la ville existe,
        // mais on peut double-vérifier ici :
        City city = cityDao.findById(cityId);
        if (city == null) {
            throw new CityNotFound("City not found: " + cityId);
        }
        return UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .queryParam("location", city.getLatitude() + "," + city.getLongitude())
                .queryParam("radius", radius)
                .queryParam("type", GOOGLE_API_TYPE)
                .queryParam("key", googlePlacesApiKey)
                .toUriString();
    }


    public List<Activity> getTop5ActivitiesByCityId(int cityId) {
        checkCityExists(cityId);
        return activityDao.findTop5ByCityId(cityId);
    }

    public void checkCityExists(int cityId) {
        City city = cityDao.findById(cityId);
        if (city == null) {
            throw new CityNotFound("City not found: " + cityId);
        }
    }
}
