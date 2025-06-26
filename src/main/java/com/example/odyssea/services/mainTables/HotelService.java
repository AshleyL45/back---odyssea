package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.CityDao;
import com.example.odyssea.daos.mainTables.HotelDao;
import com.example.odyssea.dtos.mainTables.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.HotelAlreadyExistsException;
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


    public void createHotel(HotelDto hotelDto) {
        Hotel hotel = hotelDto.toEntity();
        if (!hotelDao.cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City not found with id : " + hotel.getCityId());
        }
        hotelDao.save(hotel);
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
    public Mono<HotelDto> fetchAndSaveHotelWithStarFromAmadeusByCity(
            String iataCityCode,
            int cityId,
            int requestedStar
    ) {
        if (requestedStar != 4 && requestedStar != 5) {
            return Mono.error(new ValidationException("Only 4 or 5 stars are supported."));
        }
        String actualIata = cityDao.getIataCodeById(cityId);
        if (!iataCityCode.equalsIgnoreCase(actualIata)) {
            return Mono.error(new HotelNotFound(
                    "Mismatched IATA code " + iataCityCode + " for cityId " + cityId));
        }

        List<Hotel> found;
        try {
            found = hotelDao.findByCityIdAndStarRating(cityId, requestedStar);
        } catch (HotelNotFound e) {
            // Aucun en base => on continue
            found = Collections.emptyList();
        }
        if (!found.isEmpty()) {
            return Mono.just(HotelDto.fromEntity(found.get(0)));
        }

        return tokenService.getValidToken()
                .flatMap(token ->
                        webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/v1/reference-data/locations/hotels/by-city")
                                        .queryParam("cityCode", iataCityCode)
                                        .build()
                                )
                                .header("Authorization", "Bearer " + token)
                                .retrieve()
                                .bodyToMono(JsonNode.class)
                )
                .flatMap(json -> {
                    JsonNode node = json.path("data").path(0);
                    if (node.isMissingNode()) {
                        return Mono.error(new HotelNotFound(
                                "No hotel found for city: " + iataCityCode));
                    }

                    HotelDto dto4 = mapJsonNodeToHotelDto(node, cityId);
                    dto4.setStarRating(4);
                    safeSave(dto4);

                    HotelDto dto5 = mapJsonNodeToHotelDto(node, cityId);
                    dto5.setStarRating(5);
                    safeSave(dto5);

                    return Mono.just(requestedStar == 4 ? dto4 : dto5);
                });
    }

    private void safeSave(HotelDto dto) {
        try {
            createHotel(dto);
        } catch (HotelAlreadyExistsException e) {
        }
    }


}