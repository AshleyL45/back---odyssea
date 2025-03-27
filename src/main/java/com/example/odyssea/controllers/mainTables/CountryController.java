package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.entities.mainTables.Country;
import com.example.odyssea.services.mainTables.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Récupère tous les pays disponibles
     */
    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    /**
     * Récupère un pays spécifique par son identifiant
     */
    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable int id) {
        return countryService.getCountry(id);
    }

    /**
     * Crée un nouveau pays
     */
    @PostMapping
    public void createCountry(@RequestBody Country country) {
        countryService.createCountry(country);
    }

    /**
     * Met à jour un pays existant
     */
    @PutMapping("/{id}")
    public boolean updateCountry(@PathVariable int id, @RequestBody Country country) {
        return countryService.updateCountry(id, country);
    }

    /**
     * Supprime un pays par son identifiant
     */
    @DeleteMapping("/{id}")
    public boolean deleteCountry(@PathVariable int id) {
        return countryService.deleteCountry(id);
    }

    /**
     * Endpoint pour obtenir tous les pays d'un continent donné
     */
    @GetMapping("/by-continent")
    public List<Country> getCountriesByContinent(@RequestParam String continent) {
        return countryService.getCountriesByContinent(continent);
    }

    /**
     * Endpoint pour obtenir le pays correspondant au nom d'une ville
     */
    @GetMapping("/by-city")
    public Country getCountryByCityName(@RequestParam String cityName) {
        return countryService.getCountryByCityName(cityName);
    }
}
