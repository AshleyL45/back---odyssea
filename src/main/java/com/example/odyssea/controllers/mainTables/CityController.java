package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.services.mainTables.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
@Tag(name = "Cities", description = "Endpoints for managing cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "Get all cities", description = "Retrieve a list of all cities")
    @GetMapping
    public ResponseEntity<ApiResponse<List<City>>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(ApiResponse.success("All cities retrieved successfully", cities, HttpStatus.OK));
    }

    @Operation(summary = "Get a city by ID", description = "Retrieve a city by its unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<City>> getCityById(
            @Parameter(description = "City ID") @PathVariable int id) {
        City city = cityService.getCityById(id);
        return ResponseEntity.ok(ApiResponse.success("City found", city, HttpStatus.OK));
    }

    @Operation(summary = "Create a new city", description = "Adds a new city to the database")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCity(@RequestBody City city) {
        cityService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("City created successfully", HttpStatus.CREATED));
    }

    @Operation(summary = "Update an existing city", description = "Updates a city's information")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCity(
            @Parameter(description = "City ID") @PathVariable int id,
            @RequestBody City city) {
        cityService.updateCity(id, city);
        return ResponseEntity.ok(ApiResponse.success("City updated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Delete a city", description = "Deletes a city by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCity(
            @Parameter(description = "City ID") @PathVariable int id) {
        cityService.deleteCity(id);
        return ResponseEntity.ok(ApiResponse.success("City deleted successfully", HttpStatus.OK));
    }

    @Operation(summary = "Get cities by country ID", description = "Returns all cities that belong to a given country")
    @GetMapping("/by-country")
    public ResponseEntity<ApiResponse<List<City>>> getCitiesByCountry(
            @Parameter(description = "Country ID") @RequestParam int countryId) {
        List<City> cities = cityService.getCitiesByCountryId(countryId);
        return ResponseEntity.ok(ApiResponse.success("Cities retrieved for country ID " + countryId, cities, HttpStatus.OK));
    }

    @Operation(summary = "Find a city by coordinates", description = "Finds a city by its latitude and longitude")
    @GetMapping("/by-coordinates")
    public ResponseEntity<ApiResponse<City>> getCityByCoordinates(
            @Parameter(description = "Latitude") @RequestParam double latitude,
            @Parameter(description = "Longitude") @RequestParam double longitude) {
        City city = cityService.getCityByCoordinates(latitude, longitude);
        return ResponseEntity.ok(ApiResponse.success("City found at given coordinates", city, HttpStatus.OK));
    }

    @Operation(summary = "Get coordinates of a city", description = "Returns latitude and longitude for a given city ID")
    @GetMapping("/{id}/coordinates")
    public ResponseEntity<ApiResponse<double[]>> getCityCoordinates(
            @Parameter(description = "City ID") @PathVariable int id) {
        double[] coords = cityService.getCityCoordinates(id);
        return ResponseEntity.ok(ApiResponse.success("Coordinates retrieved", coords, HttpStatus.OK));
    }
}
