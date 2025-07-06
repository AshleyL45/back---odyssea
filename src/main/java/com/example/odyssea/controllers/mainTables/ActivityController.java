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
