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
        return countryDao.findById(id);
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
    public void updateCountry(int id, Country country) {
        countryDao.findById(id); // throws if not found
        country.setId(id);
        countryDao.update(country);
    }

    /**
     * Supprime un pays de la base de données
     */
    public void deleteCountry(int id) {
        countryDao.findById(id); // throws if not found
        countryDao.deleteById(id);
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
        return countryDao.findByCityName(cityName);
    }
}
