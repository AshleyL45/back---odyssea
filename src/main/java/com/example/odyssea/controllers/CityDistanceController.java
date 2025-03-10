package com.example.odyssea.controllers;

import com.example.odyssea.dtos.CityDistanceDto;
import com.example.odyssea.dtos.CityDistanceInfoDto;
import com.example.odyssea.entities.CityDistance;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.example.odyssea.services.CityDistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city-distances")
public class CityDistanceController {

    private final CityDistanceService cityDistanceService;

    @Autowired
    public CityDistanceController(CityDistanceService cityDistanceService) {
        this.cityDistanceService = cityDistanceService;
    }

    /**
     * Récupère toutes les distances enregistrées entre les villes
     */
    @GetMapping
    public List<CityDistance> getAllCityDistances() {
        return cityDistanceService.getAllCityDistances();
    }

    /**
     * Récupère une distance spécifique par son identifiant
     */
    @GetMapping("/{id}")
    public CityDistance getCityDistance(@PathVariable int id) {
        return cityDistanceService.getCityDistance(id);
    }

    /**
     * Crée une nouvelle distance entre deux villes
     */
    @PostMapping
    public void createCityDistance(@RequestBody CityDistanceDto cityDistanceDto) {
        cityDistanceService.createCityDistance(cityDistanceDto);
    }

    /**
     * Met à jour une distance existante entre deux villes
     */
    @PutMapping("/{id}")
    public boolean updateCityDistance(@PathVariable int id, @RequestBody CityDistanceDto cityDistanceDto) {
        return cityDistanceService.updateCityDistance(id, cityDistanceDto);
    }

    /**
     * Supprime une distance spécifique entre deux villes
     */
    @DeleteMapping("/{id}")
    public boolean deleteCityDistance(@PathVariable int id) {
        return cityDistanceService.deleteCityDistance(id);
    }

    /**
     * Récupère les informations de distance et de durée (en heures) entre deux villes spécifiées
     */
    @GetMapping("/info")
    public CityDistanceInfoDto getDistanceInfo(@RequestParam int fromCityId, @RequestParam int toCityId) {
        return cityDistanceService.getDistanceInfoBetweenCities(fromCityId, toCityId);
    }

    @GetMapping("/between")
    public ResponseEntity<?> getDistanceBetweenCities(@RequestParam int fromCityId, @RequestParam int toCityId) {
        try {
            CityDistance cityDistance = cityDistanceService.getDistanceBetweenCities(fromCityId, toCityId);
            return ResponseEntity.ok(cityDistance);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    /**
     * Importe les informations de distance et de durée entre deux villes
     */
    @PostMapping("/import")
    public ResponseEntity<?> importCityDistance(
            @RequestParam int fromCityId,
            @RequestParam int toCityId,
            @RequestParam double fromLongitude,
            @RequestParam double fromLatitude,
            @RequestParam double toLongitude,
            @RequestParam double toLatitude,
            @RequestParam String apiKey
    ) {
        cityDistanceService.importCityDistance(fromCityId, toCityId, fromLongitude, fromLatitude, toLongitude, toLatitude, apiKey);
        return ResponseEntity.ok("Import successful");
    }

}
