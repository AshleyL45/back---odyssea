package com.example.odyssea.controllers;

import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
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
     * Récupère une activité spécifique par son identifiant
     */
    @GetMapping("/{id}")
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
     * Met à jour une activité existante
     */
    @PutMapping("/{id}")
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
    public List<Activity> getTop5ActivitiesByCityId(@RequestParam int cityId) {
        return activityService.getTop5ActivitiesByCityId(cityId);
    }

    /**
     * Importe les activités depuis l'API Amadeus pour une ville donnée
     */
    @PostMapping("/import")
    public void importActivities(@RequestParam int cityId) {
        activityService.importActivitiesFromAmadeus(cityId);
    }
}
