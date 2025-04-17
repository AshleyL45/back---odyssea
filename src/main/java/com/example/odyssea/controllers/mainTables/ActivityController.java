package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.mainTables.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.exceptions.ErrorResponse;
import com.example.odyssea.services.mainTables.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable int id) {
        return ResponseEntity.ok(activityService.getActivity(id));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createActivity(@RequestBody ActivityDto activityDto, @RequestParam int cityId) {
        activityService.createActivity(activityDto, cityId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Activity created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateActivity(@PathVariable int id, @RequestBody ActivityDto activityDto, @RequestParam int cityId) {
        Activity activity = activityDto.toActivity(cityId);
        activityService.updateActivity(id, activity);
        return ResponseEntity.ok(Map.of("message", "Activity updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteActivity(@PathVariable int id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok(Map.of("message", "Activity deleted successfully"));
    }

    @GetMapping("/top5")
    public ResponseEntity<?> getTop5ActivitiesByCityId(@RequestParam int cityId) {
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);
        String message = activities.size() == 5 ? "5 activities found." : "Only " + activities.size() + " activities found.";
        return ResponseEntity.ok(Map.of("message", message, "activities", activities));
    }

    @PostMapping("/importAndGet")
    public ResponseEntity<?> importAndGetActivities(@RequestParam int cityId, @RequestParam(defaultValue = "1000") int radius) {
        activityService.checkCityExists(cityId);
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);

        if (activities.size() < 5) {
            activityService.importActivitiesFromGooglePlaces(cityId, radius);
            activities = activityService.getTop5ActivitiesByCityId(cityId);
        }

        if (activities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(HttpStatus.NOT_FOUND, "No activities found", "Aucune activité pour la ville " + cityId));
        }

        String message = activities.size() == 5 ? "5 activities found." : "Only " + activities.size() + " activities found.";
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("activities", activities);
        return ResponseEntity.ok(response);
    }
}
