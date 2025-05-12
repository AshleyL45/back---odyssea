package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.dtos.mainTables.ActivityDto;
import com.example.odyssea.entities.mainTables.Activity;
import com.example.odyssea.services.mainTables.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/activities")
@Tag(name = "Activities", description = "Manage activities linked to cities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Operation(summary = "Get all activities", description = "Returns all activities from the database")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Activity>>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(ApiResponse.success("All activities retrieved successfully", activities, HttpStatus.OK));
    }

    @Operation(summary = "Get an activity by ID", description = "Returns a specific activity by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Activity>> getActivityById(
            @Parameter(description = "ID of the activity to retrieve") @PathVariable int id) {
        Activity activity = activityService.getActivity(id);
        return ResponseEntity.ok(ApiResponse.success("Activity retrieved successfully", activity, HttpStatus.OK));
    }

    @Operation(summary = "Create a new activity", description = "Creates a new activity for a given city")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createActivity(
            @RequestBody ActivityDto activityDto,
            @RequestParam @Parameter(description = "ID of the city associated with the activity") int cityId) {
        activityService.createActivity(activityDto, cityId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Activity created successfully", HttpStatus.CREATED));
    }

    @Operation(summary = "Update an existing activity", description = "Updates an existing activity for a city")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateActivity(
            @Parameter(description = "ID of the activity to update") @PathVariable int id,
            @RequestBody ActivityDto activityDto,
            @RequestParam @Parameter(description = "ID of the city to associate with the activity") int cityId) {
        Activity activity = activityDto.toActivity(cityId);
        activityService.updateActivity(id, activity);
        return ResponseEntity.ok(ApiResponse.success("Activity updated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Delete an activity", description = "Deletes an activity by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(
            @Parameter(description = "ID of the activity to delete") @PathVariable int id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok(ApiResponse.success("Activity deleted successfully", HttpStatus.OK));
    }

    @Operation(summary = "Get top 5 activities for a city", description = "Returns the 5 best-rated or most relevant activities for a city")
    @GetMapping("/top5")
    public ResponseEntity<ApiResponse<List<Activity>>> getTop5ActivitiesByCityId(
            @RequestParam @Parameter(description = "City ID to retrieve activities for") int cityId) {
        List<Activity> activities = activityService.getTop5ActivitiesByCityId(cityId);
        String message = activities.size() == 5 ?
                "Top 5 activities retrieved successfully." :
                "Only " + activities.size() + " activities found.";
        return ResponseEntity.ok(ApiResponse.success(message, activities, HttpStatus.OK));
    }

    @Operation(summary = "Import and get activities", description = "If a city has less than 5 activities, tries to import from Google Places")
    @PostMapping("/importAndGet")
    public ResponseEntity<ApiResponse<List<Activity>>> importAndGetActivities(
            @RequestParam @Parameter(description = "City ID to import/get activities for") int cityId,
            @RequestParam(defaultValue = "10000") @Parameter(description = "Search radius in meters") int radius) {

        List<Activity> activities = activityService.importAndGetActivities(cityId, radius);

        String message = activities.size() == 5
                ? "Top 5 activities retrieved successfully after import."
                : "Only " + activities.size() + " activities found after import.";

        return ResponseEntity.ok(ApiResponse.success(message, activities, HttpStatus.OK));
    }

}
