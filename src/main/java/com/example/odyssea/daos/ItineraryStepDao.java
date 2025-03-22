package com.example.odyssea.daos;


import com.example.odyssea.dtos.DailyPlanDto;
import com.example.odyssea.dtos.DailyPlanWithCityDto;
import com.example.odyssea.entities.itinerary.ItineraryStep;
import com.example.odyssea.mapper.DailyPlanDTOMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ItineraryStepDao {


    private final JdbcTemplate jdbcTemplate;

    public ItineraryStepDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ItineraryStep> itineraryStepRowMapper = (rs, rowNum) -> new ItineraryStep(
            rs.getInt("id"),
            rs.getInt("itineraryId"),
            rs.getInt("cityId"),
            rs.getInt("countryId"),
            rs.getInt("hotelId"),
            rs.getInt("position"),
            rs.getInt("dayNumber"),
            rs.getString("descriptionPerDay")
    );



    public List<ItineraryStep> findAll() {
        String sql = "SELECT * FROM itineraryStep";
        return jdbcTemplate.query(sql, itineraryStepRowMapper);
    }



    public ItineraryStep findById(int id) {
        String sql = "SELECT * FROM itineraryStep WHERE id = ?";
        return jdbcTemplate.query(sql, itineraryStepRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Itinéraire avec l'ID : " + id + " n'existe pas"));
    }

    public List<DailyPlanDto> findByItineraryId(int itineraryId){
        String sql = "SELECT city.name AS cityName, country.name AS countryName, hotel.name AS hotelName, hotel.description AS hotelDescription, activity.name AS activityName, activity.description AS activityDescription, dailyItinerary.descriptionPerDay, dailyItinerary.dayNumber FROM dailyItinerary\n" +
                "INNER JOIN city ON dailyItinerary.cityId = city.id\n" +
                "INNER JOIN country ON dailyItinerary.countryId = country.id\n" +
                "INNER JOIN hotel ON dailyItinerary.hotelId = hotel.id\n" +
                "INNER JOIN activity ON dailyItinerary.activityId = activity.id where dailyItinerary.itineraryId = ? ORDER BY dayNumber";
        return jdbcTemplate.query(sql, new DailyPlanDTOMapper(), itineraryId);
    }


    public ItineraryStep save(ItineraryStep itineraryStep) {
        String sql = "INSERT INTO itineraryStep (itineraryId, cityId, countryId, hotelId, position, dayNumber, descriptionPerDay) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, itineraryStep.getItineraryId(), itineraryStep.getIdCity(), itineraryStep.getIdCountry(), itineraryStep.getIdHotel(), itineraryStep.getPosition(), itineraryStep.getDayNumber(), itineraryStep.getDescriptionPerDay());


        return itineraryStep;
    }



    public ItineraryStep update(int id, ItineraryStep itineraryStep) {
        if (!itineraryStepExists(id)) {
            throw new RuntimeException("Etape de l'itinéraire avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE itineraryStep SET itineraryId = ?, cityId =?, countryId = ?, hotelId = ?, position = ?, dayNumber = ?, descriptionPerDay = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itineraryStep.getPosition(), itineraryStep.getDayNumber(), itineraryStep.getDescriptionPerDay(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }


    private boolean itineraryStepExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM itineraryStep WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM itineraryStep WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public List<DailyPlanWithCityDto> findByItineraryIdWithCity(int itineraryId) {
        String sql = "SELECT " +
                "city.name AS cityName, " +
                "country.name AS countryName, " +
                "hotel.name AS hotelName, " +
                "hotel.description AS hotelDescription, " +
                "activity.name AS activityName, " +
                "activity.description AS activityDescription, " +
                "dailyItinerary.descriptionPerDay, " +
                "dailyItinerary.dayNumber, " +
                "city.latitude, " +
                "city.longitude " +
                "FROM dailyItinerary " +
                "INNER JOIN city ON dailyItinerary.cityId = city.id " +
                "INNER JOIN country ON dailyItinerary.countryId = country.id " +
                "INNER JOIN hotel ON dailyItinerary.hotelId = hotel.id " +
                "INNER JOIN activity ON dailyItinerary.activityId = activity.id " +
                "WHERE dailyItinerary.itineraryId = ? " +
                "ORDER BY dayNumber";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> DailyPlanWithCityDto.fromResultSet(rs), itineraryId);
    }


}
