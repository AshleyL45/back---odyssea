package com.example.odyssea.controllers;

import com.example.odyssea.dtos.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.services.ActivityService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/importAndGet")
    public List<Activity> importAndGetActivities(@RequestParam int cityId) {
        // Importation des activités depuis Amadeus pour la ville donnée
        activityService.importActivitiesFromAmadeus(cityId);
        // Récupération des 5 activités enregistrées dans la base
        return activityService.getTop5ActivitiesByCityId(cityId);
    }
}


