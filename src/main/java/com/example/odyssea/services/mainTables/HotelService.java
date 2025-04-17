package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.HotelDao;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.HotelNotFound;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.example.odyssea.exceptions.ValidationException;
import com.example.odyssea.services.amadeus.TokenService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
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

    public HotelService(HotelDao hotelDao,
                        TokenService tokenService,
                        WebClient.Builder webClientBuilder,
                        CityDao cityDao) {
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
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
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

    public List<Hotel> getHotelsByCityAndStarRating(int cityId, int starRating) {
        return hotelDao.findByCityIdAndStarRating(cityId, starRating);
    }

    private HotelDto mapJsonNodeToHotelDto(JsonNode node, int cityId) {
        HotelDto dto = new HotelDto();
        dto.setCityId(cityId);

        String rawName = node.path("name").asText("Unknown name");
        dto.setName(toTitleCase(rawName));

        String rawDescription = node.path("description").asText("Hotel from Amadeus");

        if (rawDescription.equalsIgnoreCase("Hotel from Amadeus")) {
            rawDescription = "No description for this hotel";
        } else {
            rawDescription = toTitleCase(rawDescription);
        }

        dto.setDescription(rawDescription);

        // Prix
        dto.setPrice(minPrice + random.nextDouble() * (maxPrice - minPrice));
        return dto;
    }


    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1));
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    /**
     * En un appel, récupère ou crée et renvoie l’hôtel pour une ville et un standing donnés.
     */
    public Mono<HotelDto> fetchAndSaveHotelWithStarFromAmadeusByCity(String iataCityCode,
                                                                     int cityId,
                                                                     int requestedStar) {

        if (requestedStar != 4 && requestedStar != 5) {
            return Mono.error(new ValidationException("Only 4 or 5 stars are supported."));
        }

        String actualIata = cityDao.getIataCodeById(cityId);
        if (!iataCityCode.equalsIgnoreCase(actualIata)) {
            return Mono.error(new HotelNotFound(
                    "Mismatched IATA code " + iataCityCode + " for cityId " + cityId));
        }

        try {
            List<Hotel> existing = hotelDao.findByCityIdAndStarRating(cityId, requestedStar);
            if (!existing.isEmpty()) {
                return Mono.just(HotelDto.fromEntity(existing.get(0)));
            }
        } catch (HotelNotFound e) {
        }

        // Appel unique à Amadeus
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
                            JsonNode data = json.path("data").path(0);
                            if (data.isMissingNode()) {
                                return Mono.error(new HotelNotFound(
                                        "No hotel found for city: " + iataCityCode));
                            }

                            HotelDto dto4 = mapJsonNodeToHotelDto(data, cityId);
                            dto4.setStarRating(4);
                            createHotel(dto4);
                            HotelDto dto5 = mapJsonNodeToHotelDto(data, cityId);
                            dto5.setStarRating(5);
                            createHotel(dto5);

                            return Mono.just(requestedStar == 5 ? dto5 : dto4);
                        })
        );
    }
}