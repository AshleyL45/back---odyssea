package com.example.odyssea.services;

import com.example.odyssea.daos.CityDistanceDao;
import com.example.odyssea.dtos.CityDistanceInfoDto;
import com.example.odyssea.dtos.CityDistanceDto;
import com.example.odyssea.entities.CityDistance;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class CityDistanceService {
    private final CityDistanceDao cityDistanceDao;

    @Autowired
    public CityDistanceService(CityDistanceDao cityDistanceDao) {
        this.cityDistanceDao = cityDistanceDao;
    }

    /**
     * Récupère toutes les distances entre villes enregistrées dans la base de données.
     */
    public List<CityDistance> getAllCityDistances() {
        return cityDistanceDao.findAll();
    }

    /**
     * Récupère une distance spécifique entre deux villes par son identifiant.
     */
    public CityDistance getCityDistance(int id) {
        return cityDistanceDao.findById(id).orElse(null);
    }

    /**
     * Crée une nouvelle entrée CityDistance dans la base de données.
     */
    public void createCityDistance(CityDistanceDto dto) {
        CityDistance cityDistance = dto.toCityDistance();
        cityDistanceDao.save(cityDistance);
    }

    /**
     * Met à jour une entrée CityDistance existante.
     */
    public boolean updateCityDistance(int id, CityDistanceDto dto) {
        if (!cityDistanceDao.existsById(id)) {
            return false;
        }
        CityDistance cityDistance = dto.toCityDistance();
        cityDistance.setId(id);
        cityDistanceDao.update(cityDistance);
        return true;
    }

    /**
     * Supprime une entrée CityDistance de la base de données.
     */
    public boolean deleteCityDistance(int id) {
        if (!cityDistanceDao.existsById(id)) {
            return false;
        }
        cityDistanceDao.deleteById(id);
        return true;
    }

    /**
     * Récupère la distance et la durée entre deux villes et convertit la durée en heures.
     */
    public CityDistanceInfoDto getDistanceInfoBetweenCities(int fromCityId, int toCityId) {
        CityDistance cityDistance = cityDistanceDao.findByCityIds(fromCityId, toCityId)
                .orElseThrow(() -> new ResourceNotFoundException("No distance information found for cities " + fromCityId + " and " + toCityId));

        // Conversion de la durée de secondes en heures.
        double durationHours = cityDistance.getDrivingDurationSeconds() / 3600.0;

        return new CityDistanceInfoDto(cityDistance.getDistanceKm(), durationHours);
    }

    /**
     * Importe les informations de distance et de durée entre deux villes via l'API OpenRouteService.
     */
    public void importCityDistance(int fromCityId, int toCityId,
                                   double fromLongitude, double fromLatitude,
                                   double toLongitude, double toLatitude,
                                   String apiKey) {

        // Construction de l'URL pour appeler l'API OpenRouteService
        String url = String.format("https://api.openrouteservice.org/v2/directions/driving-car?start=%f,%f&end=%f,%f",
                fromLongitude, fromLatitude, toLongitude, toLatitude);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode features = root.path("features");

                if (features.isArray() && features.size() > 0) {
                    JsonNode firstFeature = features.get(0);
                    JsonNode properties = firstFeature.path("properties");
                    JsonNode summary = properties.path("summary");

                    double duration = summary.path("duration").asDouble();
                    double distance = summary.path("distance").asDouble();

                    CityDistanceDto dto = new CityDistanceDto();
                    dto.setFromCityId(fromCityId);
                    dto.setToCityId(toCityId);
                    dto.setDuration(duration);
                    dto.setDistance(distance);

                    CityDistance cityDistance = dto.toCityDistance();
                    cityDistanceDao.save(cityDistance);
                }
            } else {
                System.out.println("Erreur lors de l'appel à OpenRouteService : " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
