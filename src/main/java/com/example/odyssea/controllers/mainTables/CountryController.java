package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.services.mainTables.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@Tag(name = "Countries", description = "Endpoints for managing countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "Get all countries", description = "Retrieve a list of all countries")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Country>>> getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(ApiResponse.success("All countries retrieved successfully", countries, HttpStatus.OK));
    }

    @Operation(summary = "Get a country by ID", description = "Retrieve a country by its unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Country>> getCountryById(
            @Parameter(description = "Country ID") @PathVariable int id) {
        Country country = countryService.getCountry(id);
        return ResponseEntity.ok(ApiResponse.success("Country retrieved successfully", country, HttpStatus.OK));
    }

    @Operation(summary = "Create a new country", description = "Adds a new country to the database")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCountry(@RequestBody Country country) {
        countryService.createCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Country created successfully", HttpStatus.CREATED));
    }

    @Operation(summary = "Update an existing country", description = "Updates a country's information")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCountry(
            @Parameter(description = "Country ID") @PathVariable int id,
            @RequestBody Country country) {
        countryService.updateCountry(id, country);
        return ResponseEntity.ok(ApiResponse.success("Country updated successfully", HttpStatus.OK));
    }

    @Operation(summary = "Delete a country", description = "Deletes a country by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCountry(
            @Parameter(description = "Country ID") @PathVariable int id) {
        countryService.deleteCountry(id);
        return ResponseEntity.ok(ApiResponse.success("Country deleted successfully", HttpStatus.OK));
    }

    @Operation(summary = "Get countries by continent", description = "Returns all countries that belong to a given continent")
    @GetMapping("/by-continent")
    public ResponseEntity<ApiResponse<List<Country>>> getCountriesByContinent(
            @Parameter(description = "Continent name") @RequestParam String continent) {
        List<Country> countries = countryService.getCountriesByContinent(continent);
        return ResponseEntity.ok(ApiResponse.success("Countries retrieved for continent: " + continent, countries, HttpStatus.OK));
    }

    @Operation(summary = "Get country by city name", description = "Returns the country of a given city name")
    @GetMapping("/by-city")
    public ResponseEntity<ApiResponse<Country>> getCountryByCityName(
            @Parameter(description = "City name") @RequestParam String cityName) {
        Country country = countryService.getCountryByCityName(cityName);
        return ResponseEntity.ok(ApiResponse.success("Country retrieved for city: " + cityName, country, HttpStatus.OK));
    }
}
