package com.example.odyssea.services;

import com.example.odyssea.daos.CityDistanceDao;
import com.example.odyssea.dtos.CityDistanceDto;
import com.example.odyssea.dtos.CityDistanceInfoDto;
import com.example.odyssea.entities.CityDistance;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CityDistanceService {

    private final CityDistanceDao cityDistanceDao;

    public CityDistanceService(CityDistanceDao cityDistanceDao) {
        this.cityDistanceDao = cityDistanceDao;
    }

    public List<CityDistance> getAllCityDistances() {
        return cityDistanceDao.findAll();
    }

    public CityDistance getCityDistance(int id) {
        return cityDistanceDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CityDistance not found with id: " + id));
    }

    public void createCityDistance(CityDistanceDto dto) {
        cityDistanceDao.save(dto.toCityDistance());
    }

    public boolean updateCityDistance(int id, CityDistanceDto dto) {
        if (!cityDistanceDao.existsById(id)) {
            return false;
        }
        CityDistance cityDistance = dto.toCityDistance();
        cityDistance.setId(id);
        cityDistanceDao.save(cityDistance);
        return true;
    }

    public boolean deleteCityDistance(int id) {
        if (!cityDistanceDao.existsById(id)) {
            return false;
        }
        cityDistanceDao.deleteById(id);
        return true;
    }

    public CityDistanceInfoDto getDistanceInfoBetweenCities(int fromCityId, int toCityId) {
        CityDistance cityDistance = getDistanceBetweenCities(fromCityId, toCityId);
        double durationHours = cityDistance.getDrivingDurationSeconds() / 3600.0;
        return new CityDistanceInfoDto(cityDistance.getDistanceKm(), durationHours);
    }

    public CityDistance getDistanceBetweenCities(int fromCityId, int toCityId) {
        return cityDistanceDao.findByCityIds(fromCityId, toCityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CityDistance not found between cities " + fromCityId + " and " + toCityId
                ));
    }

    public void importCityDistance(int fromCityId, int toCityId,
                                   double fromLongitude, double fromLatitude,
                                   double toLongitude, double toLatitude,
                                   String apiKey) {

        String url = String.format(java.util.Locale.US,
                "https://api.openrouteservice.org/v2/directions/driving-car?start=%f,%f&end=%f,%f",
                fromLongitude, fromLatitude, toLongitude, toLatitude);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode summary = new ObjectMapper().readTree(response.getBody())
                        .path("features").get(0).path("properties").path("summary");

                double duration = summary.path("duration").asDouble();
                double distance = summary.path("distance").asDouble();

                CityDistanceDto dto = new CityDistanceDto();
                dto.setFromCityId(fromCityId);
                dto.setToCityId(toCityId);
                dto.setDuration(duration);
                dto.setDistance(distance);

                cityDistanceDao.save(dto.toCityDistance());
            } else {
                throw new RuntimeException("Erreur appel API externe : " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à OpenRouteService", e);
        }
    }
}