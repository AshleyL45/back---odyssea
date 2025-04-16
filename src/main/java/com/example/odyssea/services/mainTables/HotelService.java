package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.HotelDao;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.example.odyssea.services.amadeus.TokenService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Random;

@Service
public class HotelService {

    private final HotelDao hotelDao;
    private final TokenService tokenService;
    private final CityDao cityDao;
    private final WebClient webClient;

    @Value("${amadeus.hotel.minPrice:150}")
    private double minPrice;

    @Value("${amadeus.hotel.maxPrice:400}")
    private double maxPrice;

    private final Random random = new Random();

    public HotelService(HotelDao hotelDao, TokenService tokenService, WebClient.Builder webClientBuilder, CityDao cityDao) {
        this.hotelDao = hotelDao;
        this.tokenService = tokenService;
        this.cityDao = cityDao;
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com").build();
    }

    public List<Hotel> getAllHotels() {
        return hotelDao.findAll();
    }

    public Mono<Hotel> getHotel(int id) {
        return Mono.fromCallable(() -> hotelDao.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty));
    }

    public void createHotel(HotelDto hotelDto) {
        Hotel hotel = hotelDto.toEntity();
        if (!hotelDao.cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City not found with id : " + hotel.getCityId());
        }
        hotelDao.save(hotel);
    }

    public boolean updateHotel(int id, HotelDto hotelDto) {
        if (!hotelDao.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id : " + id);
        }
        Hotel hotel = hotelDto.toEntity();
        hotel.setId(id);
        hotelDao.update(hotel);
        return true;
    }

    public boolean deleteHotel(int id) {
        if (!hotelDao.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id : " + id);
        }
        hotelDao.deleteById(id);
        return true;
    }

    public List<Hotel> getHotelsByCityId(int cityId) {
        List<Hotel> hotels = hotelDao.findByCityId(cityId);

        if (!hotels.isEmpty()) {
            Hotel randomHotel = hotels.get(random.nextInt(hotels.size()));
            return List.of(randomHotel);
        }

        String iataCode = cityDao.getIataCodeById(cityId);
        String token = tokenService.getValidToken().block();

        JsonNode json = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/reference-data/locations/hotels/by-city")
                        .queryParam("cityCode", iataCode)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (json.has("data") && json.get("data").isArray() && json.get("data").size() > 0) {
            JsonNode firstHotelNode = json.get("data").get(0);
            HotelDto dto = mapJsonNodeToHotelDto(firstHotelNode, cityId);
            createHotel(dto);
            return List.of(dto.toEntity());
        } else {
            throw new ResourceNotFoundException("No hotels found in Amadeus API for cityId " + cityId);
        }
    }

    public List<Hotel> getHotelsByCityAndStarRating(int cityId, int starRating) {
        List<Hotel> hotels = hotelDao.findByCityIdAndStarRating(cityId, starRating);
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found for city id: " + cityId
                    + " with star rating: " + starRating);
        }
        return hotels;
    }

    public Mono<HotelDto> fetchHotelDataFromAmadeus(String amadeusHotelId) {
        return tokenService.getValidToken().flatMap(token ->
                webClient.get()
                        .uri("/v1/hotels/" + amadeusHotelId)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(HotelDto.class)
        );
    }

    private HotelDto mapJsonNodeToHotelDto(JsonNode node, int cityId) {
        HotelDto dto = new HotelDto();
        dto.setCityId(cityId);
        dto.setName(node.has("name") ? node.get("name").asText() : "Unknown name");
        dto.setStarRating((random.nextDouble() < 0.5) ? 4 : 5);
        String hotelId = node.has("hotelId") ? node.get("hotelId").asText() : "unknown";
        dto.setDescription("Hotel from Amadeus: " + hotelId);
        double randomPrice = minPrice + (random.nextDouble() * (maxPrice - minPrice));
        dto.setPrice(randomPrice);
        return dto;
    }

    public Mono<HotelDto> fetchAndSaveOneHotelFromAmadeusByCity(String iataCityCode, int cityId) {
        return tokenService.getValidToken().flatMap(token ->
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v1/reference-data/locations/hotels/by-city")
                                .queryParam("cityCode", iataCityCode)
                                .build())
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .flatMap(json -> {
                            if (json.has("data") && json.get("data").isArray() && json.get("data").size() > 0) {
                                JsonNode hotelNode = json.get("data").get(0);
                                HotelDto dto = mapJsonNodeToHotelDto(hotelNode, cityId);
                                createHotel(dto);
                                return Mono.just(dto);
                            } else {
                                return Mono.error(new ResourceNotFoundException("No hotel found for city: " + iataCityCode));
                            }
                        })
        );
    }
}