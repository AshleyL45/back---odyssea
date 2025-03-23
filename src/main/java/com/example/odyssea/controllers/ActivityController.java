package com.example.odyssea.controllers;

import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.services.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Récupère toutes les activités disponibles
     */
    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    /**
     * Récupère une activité spécifique par son identifiant (uniquement numérique)
     */
    @GetMapping("/{id:\\d+}")
    public Activity getActivityById(@PathVariable int id) {
        return activityService.getActivity(id);
    }

    /**
     * Crée une nouvelle activité pour une ville donnée
     */
    @PostMapping
    public void createActivity(@RequestBody ActivityDto activityDto, @RequestParam int cityId) {
        activityService.createActivity(activityDto, cityId);
    }

    /**
     * Met à jour une activité existante (identifiant numérique uniquement)
     */
    @PutMapping("/{id:\\d+}")
    public boolean updateActivity(@PathVariable int id, @RequestBody ActivityDto activityDto, @RequestParam int cityId) {
        Activity activity = activityDto.toActivity(cityId);
        return activityService.updateActivity(id, activity);
    }

    /**
     * Supprime une activité par son identifiant
     */
    @DeleteMapping("/{id}")
    public boolean deleteActivity(@PathVariable int id) {
        return activityService.deleteActivity(id);
    }

    /**
     * Récupère les 5 meilleures activités d'une ville spécifique
     */
    @GetMapping("/top5")
    public ResponseEntity<?> getTop5ActivitiesByCityId(@RequestParam int cityId) {
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);
        if(activities == null){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Collections.singletonMap("message", "Sorry, there are no activities for this city."));
        }
        return ResponseEntity.ok(activities);
    }

    /**
     * Importe les activités depuis l'API Amadeus pour une ville donnée
     */
    @PostMapping("/importAndGet")
    public ResponseEntity<?> importAndGetActivities(@RequestParam int cityId, @RequestParam int radius) {
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);
        if (activities.size() < 5) {
            activityService.importActivitiesFromAmadeus(cityId, radius);
            activities = activityService.getTop5ActivitiesByCityId(cityId);
        }
        if (activities.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Sorry, there are no activities for this city."));
        }
        return ResponseEntity.ok(activities);
    }



}


