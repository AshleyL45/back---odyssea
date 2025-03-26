package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.entities.mainTables.City;
import com.example.odyssea.services.mainTables.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Récupère la liste de toutes les villes
     */
    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    /**
     * Récupère une ville spécifique par son identifiant
     */
    @GetMapping("/{id}")
    public Optional<City> getCityById(@PathVariable int id) {
        return cityService.getCityById(id);
    }

    /**
     */
    @PostMapping
    public void createCity(@RequestBody City city) {
        cityService.createCity(city);
    }

    /**
     * Met à jour une ville existante
     */
    @PutMapping("/{id}")
    public boolean updateCity(@PathVariable int id, @RequestBody City city) {
        return cityService.updateCity(id, city);
    }

    /**
     * Supprime une ville par son identifiant
     */
    @DeleteMapping("/{id}")
    public boolean deleteCity(@PathVariable int id) {
        return cityService.deleteCity(id);
    }

    /**
     * Récupère les villes appartenant à un pays donné
     */
    @GetMapping("/by-country")
    public List<City> getCitiesByCountry(@RequestParam int countryId) {
        return cityService.getCitiesByCountryId(countryId);
    }

    /**
     * Retrouve une ville par ses coordonnées géographiques
     */
    @GetMapping("/by-coordinates")
    public Optional<City> getCityByCoordinates(@RequestParam double latitude, @RequestParam double longitude) {
        return cityService.getCityByCoordinates(latitude, longitude);
    }

    /**
     * Obtient uniquement les coordonnées (latitude et longitude) d'une ville par son identifiant
     */
    @GetMapping("/{id}/coordinates")
    public Optional<double[]> getCityCoordinates(@PathVariable int id) {
        return cityService.getCityCoordinates(id);
    }

}
