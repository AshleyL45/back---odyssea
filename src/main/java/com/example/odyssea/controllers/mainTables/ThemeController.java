package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.entities.mainTables.Theme;
import com.example.odyssea.services.mainTables.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@Tag(name = "Themes", description = "Handles all operations related to themes.")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Operation(
            summary = "Get all themes",
            description = "Returns the list of all available themes in the system."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<Theme>>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();
        return ResponseEntity.ok(
                ApiResponse.success("Themes retrieved successfully.", themes, HttpStatus.OK)
        );
    }
}
