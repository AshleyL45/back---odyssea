package com.example.odyssea.services;

import com.example.odyssea.daos.HotelDao;
import com.example.odyssea.dtos.HotelDto;
import com.example.odyssea.entities.mainTables.Hotel;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class HotelService {

    private final HotelDao hotelDao;
    private final TokenService tokenService;
    private final WebClient webClient;

    // Plage de prix configurable
    @Value("${amadeus.hotel.minPrice:150}")
    private double minPrice;

    @Value("${amadeus.hotel.maxPrice:400}")
    private double maxPrice;

    // Instance unique de Random pour générer des valeurs aléatoires
    private final Random random = new Random();

    public HotelService(HotelDao hotelDao, TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.hotelDao = hotelDao;
        this.tokenService = tokenService;
        // Utilisation de l'environnement sandbox
        this.webClient = webClientBuilder.baseUrl("https://test.api.amadeus.com").build();
    }

    /**
     * Retourne tous les hôtels enregistrés en base
     */
    public List<Hotel> getAllHotels() {
        return hotelDao.findAll();
    }

    /**
     * Retourne un hôtel par son identifiant de manière asynchrone
     * Si l'hôtel n'existe pas en base, il est récupéré via l'API Amadeus, enregistré, puis renvoyé
     */
    public Mono<Hotel> getHotel(int id) {
        return Mono.fromCallable(() -> hotelDao.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optional -> {
                    if (optional.isPresent()) {
                        return Mono.just(optional.get());
                    } else {
                        return fetchHotelDataFromAmadeus(String.valueOf(id))
                                .flatMap(dto ->
                                        Mono.fromCallable(() -> {
                                            if (!hotelDao.cityExists(dto.toEntity().getCityId())) {
                                                throw new ResourceNotFoundException("City not found with id "
                                                        + dto.toEntity().getCityId());
                                            }
                                            hotelDao.save(dto.toEntity());
                                            return dto.toEntity();
                                        }).subscribeOn(Schedulers.boundedElastic())
                                );
                    }
                });
    }

    /**
     * Enregistre un nouvel hôtel dans la base
     */
    public void createHotel(HotelDto hotelDto) {
        Hotel hotel = hotelDto.toEntity();
        if (!hotelDao.cityExists(hotel.getCityId())) {
            throw new ResourceNotFoundException("City not found with id : " + hotel.getCityId());
        }
        hotelDao.save(hotel);
    }

    /**
     * Met à jour un hôtel existant
     */
    public boolean updateHotel(int id, HotelDto hotelDto) {
        if (!hotelDao.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id : " + id);
        }
        Hotel hotel = hotelDto.toEntity();
        hotel.setId(id);
        hotelDao.update(hotel);
        return true;
    }

    /**
     * Supprime un hôtel de la base
     */
    public boolean deleteHotel(int id) {
        if (!hotelDao.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id : " + id);
        }
        hotelDao.deleteById(id);
        return true;
    }

    /**
     * Retourne les hôtels d'une ville donnée
     */
    public List<Hotel> getHotelsByCityId(int cityId) {
        List<Hotel> hotels = hotelDao.findByCityId(cityId);
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found for id : " + cityId);
        }
        return hotels;
    }

    /**
     * Retourne les hôtels d'une ville ayant un certain classement (nombre d'étoiles)
     */
    public List<Hotel> getHotelsByCityAndStarRating(int cityId, int starRating) {
        try  {
            List<Hotel> hotels = hotelDao.findByCityIdAndStarRating(cityId, starRating);
            if (hotels.isEmpty()) {
                throw new ResourceNotFoundException("No hotels found for city id: " + cityId
                        + " with star rating: " + starRating);
            }
            return hotels;
        } catch (ResourceNotFoundException e){
            return null;
        }

    }

    /**
     * Récupère les données d'un hôtel via l'API Amadeus en utilisant un token valide
     */
    public Mono<HotelDto> fetchHotelDataFromAmadeus(String amadeusHotelId) {
        return tokenService.getValidToken().flatMap(token ->
                webClient.get()
                        .uri("/v1/hotels/" + amadeusHotelId)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(HotelDto.class)
        );
    }

    /**
     * Récupère les données d'un hôtel via l'API Amadeus et l'enregistre dans la base
     */
    public Mono<Void> createHotelFromAmadeus(String amadeusHotelId) {
        return fetchHotelDataFromAmadeus(amadeusHotelId)
                .doOnNext(this::createHotel)
                .then();
    }


    /**
     * Mappe un JsonNode en un objet HotelDto.
     *
     * @param node   Le JsonNode contenant les données de l'hôtel
     * @param cityId L'identifiant de la ville dans la BDD
     * @return Un objet HotelDto avec les données mappées
     */
    private HotelDto mapJsonNodeToHotelDto(JsonNode node, int cityId) {
        HotelDto dto = new HotelDto();
        dto.setCityId(cityId);
        dto.setName(node.has("name") ? node.get("name").asText() : "Unknown name");
        // Attribution aléatoire d'un classement : 4 ou 5 étoiles
        dto.setStarRating((random.nextDouble() < 0.5) ? 4 : 5);
        String hotelId = node.has("hotelId") ? node.get("hotelId").asText() : "unknown";
        dto.setDescription("Hotel from Amadeus: " + hotelId);
        double randomPrice = minPrice + (random.nextDouble() * (maxPrice - minPrice));
        dto.setPrice(randomPrice);
        return dto;
    }

    /**
     * Sélectionne exactement 5 hôtels dans la liste fournie en s'assurant qu'il y ait au moins un hôtel 4 étoiles et un hôtel 5 étoiles.
     *
     * @param list La liste d'hôtels récupérés
     * @return Une Mono contenant une liste de 5 HotelDto répondant aux contraintes
     */
    private Mono<List<HotelDto>> selectFiveHotels(List<HotelDto> list) {
        if (list.size() < 5) {
            return Mono.error(new Exception("Insufficient number of hotels recovered to satisfy the constraint."));
        }
        List<HotelDto> group4 = list.stream().filter(h -> h.getStarRating() == 4).toList();
        List<HotelDto> group5 = list.stream().filter(h -> h.getStarRating() == 5).toList();
        if (group4.isEmpty() || group5.isEmpty()) {
            return Mono.error(new Exception("The recovered hotels do not contain at least one 4-star and one 5-star hotel."));
        }
        List<HotelDto> selected = new ArrayList<>();
        selected.add(group4.get(0));
        selected.add(group5.get(0));
        for (HotelDto h : list) {
            if (selected.size() >= 5) break;
            if (!selected.contains(h)) {
                selected.add(h);
            }
        }
        if (selected.size() != 5) {
            return Mono.error(new Exception("Impossible to select exactly 5 hotels."));
        }
        return Mono.just(selected);
    }

    /**
     * Récupère les hôtels via l'API Amadeus en fonction du code IATA et garantit que la ville aura exactement 5 hôtels enregistrés.
     * Si la BDD contient déjà 5 hôtels pour le cityId donné, ils sont retournés directement.
     * Sinon, on récupère les hôtels depuis l'API, on sélectionne exactement 5 hôtels (avec au moins un hôtel 4 étoiles et un hôtel 5 étoiles),
     * on insère ceux qui ne sont pas déjà présents dans la BDD, puis on retourne 5 hôtels.
     *
     * @param iataCityCode Le code IATA de la ville (ex. "MAD")
     * @param cityId       L'identifiant de la ville dans la BDD
     * @return Une liste de 5 HotelDto répondant aux critères
     */
    public Mono<List<HotelDto>> fetchAndSaveHotelsFromAmadeusByCity(String iataCityCode, int cityId) {
        // Si la BDD contient déjà 5 hôtels pour cette ville on les retourne
        return Mono.fromCallable(() -> hotelDao.findByCityId(cityId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(existingHotels -> {
                    if (existingHotels.size() >= 5) {
                        List<HotelDto> dtos = existingHotels.stream()
                                .limit(5)
                                .map(HotelDto::fromEntity)
                                .toList();
                        return Mono.just(dtos);
                    } else {
                        // Sinon on appelle l'API Amadeus pour récupérer des hôtels
                        return tokenService.getValidToken().flatMap(token ->
                                        webClient.get()
                                                .uri(uriBuilder -> uriBuilder
                                                        .path("/v1/reference-data/locations/hotels/by-city")
                                                        .queryParam("cityCode", iataCityCode)
                                                        .build()
                                                )
                                                .header("Authorization", "Bearer " + token)
                                                .retrieve()
                                                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse ->
                                                        clientResponse.bodyToMono(String.class)
                                                                .flatMap(errorBody -> Mono.error(new Exception("Amadeus error: " + errorBody)))
                                                )
                                                .bodyToMono(JsonNode.class)
                                ).flatMapMany(jsonNode -> {
                                    if (jsonNode.has("data") && jsonNode.get("data").isArray()) {
                                        return Flux.fromIterable(jsonNode.get("data"))
                                                .map(node -> mapJsonNodeToHotelDto(node, cityId));
                                    } else {
                                        return Flux.empty();
                                    }
                                }).collectList().flatMap(this::selectFiveHotels)
                                .flatMap(selectedList ->
                                        // Insertion dans la base uniquement des hôtels non déjà présents
                                        Mono.fromCallable(() -> {
                                            for (HotelDto dto : selectedList) {
                                                if (!hotelDao.existsByNameAndCityId(dto.getName(), dto.getCityId())) {
                                                    createHotel(dto);
                                                }
                                            }
                                            List<Hotel> updated = hotelDao.findByCityId(cityId);
                                            List<HotelDto> result = updated.stream().limit(5)
                                                    .map(HotelDto::fromEntity)
                                                    .toList();
                                            return result;
                                        }).subscribeOn(Schedulers.boundedElastic())
                                );
                    }
                });
    }
}
