package com.example.odyssea.services;

import com.example.odyssea.daos.ActivityDao;
import com.example.odyssea.daos.CityDao;
import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class ActivityService {

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
        try{
            return activityDao.findTop5ByCityId(cityId);
        } catch (ResourceNotFoundException e){
            return null;
        }
    }

    /**
     * Importe les activités depuis l'API Amadeus pour une ville donnée
     * en filtrant par le code IATA, la latitude et la longitude de la ville,
     * en demandant 5 activités et en vérifiant qu'au moins 5 résultats sont obtenus
     * Seules les 5 premières activités seront insérées
     */
    public void importActivitiesFromAmadeus(int cityId, int radius) {
        // Vérifier que la ville existe, récupérer les infos, etc.
        City city = cityDao.findById(cityId).orElseThrow(() ->
                new IllegalStateException("City not found for id " + cityId));

        String cityCode = city.getIataCode();
        double latitude = city.getLatitude();
        double longitude = city.getLongitude();

        if (cityCode == null || cityCode.isEmpty()) {
            throw new IllegalStateException("IATA code for city id " + cityId + " cannot be found.");
        }

        String token = tokenService.getValidToken().block();
        if (token == null) {
            throw new IllegalStateException("Unable to retrieve Amadeus token.");
        }

        // Ajout du paramètre "radius" dans l'URL
        String url = "https://test.api.amadeus.com/v1/shopping/activities"
                + "?cityCode=" + cityCode
                + "&latitude=" + latitude
                + "&longitude=" + longitude
                + "&radius=" + radius
                + "&limit=5";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode dataArray = root.path("data");

                // Si aucune activité n'est retournée, on log et on arrête l'import
                if (!dataArray.isArray() || dataArray.size() == 0) {
                    System.out.println("No activities returned for the city with IATA code " + cityCode);
                    return;
                }

                int numberOfActivitiesToImport = Math.min(5, dataArray.size());
                for (int i = 0; i < numberOfActivitiesToImport; i++) {
                    JsonNode node = dataArray.get(i);
                    ActivityDto dto = mapper.treeToValue(node, ActivityDto.class);
                    Activity activity = dto.toActivity(cityId);
                    activityDao.save(activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error calling Amadeus API: " + response.getStatusCode());
        }
    }

}
