package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ActivityDao;
import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.dtos.mainTables.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.example.odyssea.services.amadeus.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;

import java.time.LocalTime;
import java.util.List;

@Service
public class ActivityService {

    @Value("${GOOGLE_PLACES_API_KEY}")
    private String googlePlacesApiKey;

    private final ActivityDao activityDao;
    private final TokenService tokenService;
    private final CityDao cityDao;

    public ActivityService(ActivityDao activityDao, TokenService tokenService, CityDao cityDao) {
        this.activityDao = activityDao;
        this.tokenService = tokenService;
        this.cityDao = cityDao;
    }

    /**
     * Récupère la liste complète des activités depuis la base de données
     */
    public List<Activity> getAllActivities() {
        return activityDao.findAll();
    }

    /**
     * Récupère une activité spécifique par son identifiant
     */
    public Activity getActivity(int id) {
        return activityDao.findById(id).orElse(null);
    }

    /**
     * Crée une nouvelle activité en base de données
     */
    public void createActivity(ActivityDto activityDto, int cityId) {
        if (!activityDao.cityExists(cityId)) {
            throw new IllegalArgumentException("The cityId supplied does not exist in the database!");
        }
        Activity activity = activityDto.toActivity(cityId);
        activityDao.save(activity);
    }

    /**
     * Met à jour une activité existante
     */
    public boolean updateActivity(int id, Activity activity) {
        if (!activityDao.existsById(id)) {
            return false;
        }
        activity.setId(id);
        activityDao.update(activity);
        return true;
    }

    /**
     * Supprime une activité en fonction de son identifiant
     */
    public boolean deleteActivity(int id) {
        if (!activityDao.existsById(id)) {
            return false;
        }
        activityDao.deleteById(id);
        return true;
    }

    /**
     * Récupère les 5 activités d'une ville spécifique
     */
    public List<Activity> getTop5ActivitiesByCityId(int cityId) {
        try {
            return activityDao.findTop5ByCityId(cityId);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    /**
     * Import les activités depuis Google Places et s'assure que la ville possède 5 activités uniques.
     * Pour chaque résultat de l'API, on vérifie qu'une activité avec le même nom n'est pas déjà présente,
     * et on continue à importer jusqu'à atteindre 5 activités ou épuiser les résultats.
     */
    public void importActivitiesFromGooglePlaces(int cityId, int radius) {
        // Récupère les informations de la ville
        City city = cityDao.findById(cityId).orElseThrow(() ->
                new IllegalStateException("City not found for id " + cityId));
        double latitude = city.getLatitude();
        double longitude = city.getLongitude();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&type=tourist_attraction" +
                "&key=" + googlePlacesApiKey;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                String status = root.path("status").asText();
                if (!"OK".equals(status)) {
                    System.out.println("Google Places API returned status: " + status);
                    return;
                }

                JsonNode results = root.path("results");
                if (!results.isArray() || results.size() == 0) {
                    System.out.println("No activities returned from Google Places API for city id " + cityId);
                    return;
                }

                // Récupérer le nombre actuel d'activités pour la ville
                int currentCount = activityDao.findTop5ByCityId(cityId).size();
                // Parcourir l'ensemble des résultats tant que le nombre d'activités est inférieur à 5
                for (int i = 0; i < results.size() && currentCount < 5; i++) {
                    JsonNode resultNode = results.get(i);
                    String name = resultNode.path("name").asText();
                    String vicinity = resultNode.has("vicinity") ? resultNode.path("vicinity").asText() : "";
                    String description = vicinity.isEmpty() ? "No description available" : vicinity;
                    String type = "Tourist Attraction";
                    String physicalEffort = "Low";
                    LocalTime duration = LocalTime.of(1, 0);
                    Double price = 0.0;

                    // Insérer l'activité seulement si elle n'existe pas déjà pour la ville
                    if (!activityDao.activityExists(cityId, name)) {
                        Activity activity = new Activity(0, cityId, name, type, physicalEffort, duration, description, price);
                        activityDao.save(activity);
                        currentCount++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error calling Google Places API: " + response.getStatusCode());
        }
    }
}
