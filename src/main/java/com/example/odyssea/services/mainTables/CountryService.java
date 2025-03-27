package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.CountryDao;
import com.example.odyssea.entities.mainTables.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryDao countryDao;

    @Autowired
    public CountryService(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    /**
     * Récupère tous les pays présents dans la base de données
     */
    public List<Country> getAllCountries() {
        return countryDao.findAll();
    }

    /**
     * Récupère un pays spécifique par son identifiant
     */
    public Country getCountry(int id) {
        return countryDao.findById(id).orElse(null);
    }

    /**
     * Enregistre un nouveau pays dans la base de données
     */
    public void createCountry(Country country) {
        countryDao.save(country);
    }

    /**
     * Met à jour les informations d'un pays existant
     */
    public boolean updateCountry(int id, Country country) {
        Optional<Country> existing = countryDao.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        country.setId(id);
        countryDao.update(country);
        return true;
    }

    /**
     * Supprime un pays de la base de données
     */
    public boolean deleteCountry(int id) {
        Optional<Country> existing = countryDao.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        countryDao.deleteById(id);
        return true;
    }

    /**
     * Récupère la liste des pays appartenant à un continent donné
     */
    public List<Country> getCountriesByContinent(String continent) {
        return countryDao.findByContinent(continent);
    }

    /**
     * Récupère le pays associé au nom d'une ville spécifique
     */
    public Country getCountryByCityName(String cityName) {
        return countryDao.findByCityName(cityName).orElse(null);
    }
}
