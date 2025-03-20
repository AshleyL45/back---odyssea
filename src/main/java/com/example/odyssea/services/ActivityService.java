package com.example.odyssea.services;

import com.example.odyssea.daos.ActivityDao;
import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityDao activityDao;

    @Autowired
    public ActivityService(ActivityDao activityDao) {
        this.activityDao = activityDao;
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
            throw new IllegalArgumentException("Le cityId fourni n'existe pas dans la base de données !");
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
     * Importe les activités depuis l'API Amadeus pour une ville spécifique
     */
    public void importActivitiesFromAmadeus(int cityId) {
        if (!activityDao.cityExists(cityId)) {
            throw new IllegalArgumentException("Le cityId fourni n'existe pas dans la base de données !");
        }

        // URL de l'API Amadeus
        String url = "https://test.api.amadeus.com/v1/shopping/activities";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Vérification du statut de la réponse HTTP
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode dataArray = root.path("data");

                if (dataArray.isArray()) {
                    for (JsonNode node : dataArray) {
                        ActivityDto dto = mapper.treeToValue(node, ActivityDto.class);
                        Activity activity = dto.toActivity(cityId);
                        activityDao.save(activity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur lors de l'appel à l'API Amadeus : " + response.getStatusCode());
        }
    }
}
