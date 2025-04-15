package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.mainTables.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.services.mainTables.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @GetMapping("/{id:\\d+}")
    public Activity getActivityById(@PathVariable int id) {
        return activityService.getActivity(id);
    }

    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody ActivityDto activityDto, @RequestParam int cityId) {
        activityService.createActivity(activityDto, cityId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Activity created successfully"));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<?> updateActivity(@PathVariable int id, @RequestBody ActivityDto activityDto, @RequestParam int cityId) {
        Activity activity = activityDto.toActivity(cityId);
        boolean updated = activityService.updateActivity(id, activity);
        return updated ? ResponseEntity.ok(Collections.singletonMap("message", "Activity updated successfully"))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Activity not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable int id) {
        boolean deleted = activityService.deleteActivity(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Activity deleted successfully"))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Activity not found with id: " + id));
    }

    @GetMapping("/top5")
    public ResponseEntity<?> getTop5ActivitiesByCityId(@RequestParam int cityId) {
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);
        if (activities == null || activities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No activities found for city id " + cityId));
        }
        return ResponseEntity.ok(buildActivityResponse(activities, cityId));
    }

    @PostMapping("/importAndGet")
    public ResponseEntity<?> importAndGetActivities(@RequestParam int cityId, @RequestParam int radius) {
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);
        if (activities.size() < 5) {
            activityService.importActivitiesFromGooglePlaces(cityId, radius);
            activities = activityService.getTop5ActivitiesByCityId(cityId);
        }
        if (activities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No activities found for city id " + cityId));
        }
        return ResponseEntity.ok(buildActivityResponse(activities, cityId));
    }

    private Map<String, Object> buildActivityResponse(List<Activity> activities, int cityId) {
        String message = activities.size() == 5
                ? "5 activities found."
                : "Only " + activities.size() + " activities found for city id " + cityId + ".";
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("activities", activities);
        return response;
    }
}
