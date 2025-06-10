package com.example.odyssea.daos.itinerary;


import com.example.odyssea.dtos.mainTables.DailyPlanDto;
import com.example.odyssea.dtos.mainTables.DailyPlanWithCityDto;
import com.example.odyssea.entities.itinerary.ItineraryStep;
import com.example.odyssea.exceptions.DatabaseException;
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
            rs.getInt("itinerary_id"),
            rs.getInt("city_id"),
            rs.getInt("country_id"),
            rs.getInt("hotel_id"),
            rs.getInt("position"),
            rs.getInt("day_number"),
            rs.getString("description_per_day")
    );



    public List<ItineraryStep> findAll() {
        String sql = "SELECT * FROM itinerary_step";
        return jdbcTemplate.query(sql, itineraryStepRowMapper);
    }

    public ItineraryStep findById(int id) {
        String sql = "SELECT * FROM itinerary_step WHERE id = ?";
        return jdbcTemplate.query(sql, itineraryStepRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Itinéraire avec l'ID : " + id + " n'existe pas"));
    }

    public List<DailyPlanDto> findByItineraryId(int itineraryId){
        String sql = "SELECT city.name AS cityName, country.name AS countryName, hotel.name AS hotelName, hotel.description AS hotelDescription, activity.name AS activityName, activityDescription AS activityDescription, daily_itinerary.description_per_day, daily_itinerary.day_number FROM daily_itinerary\n" +
                "INNER JOIN city ON daily_itinerary.city_id = city.id\n" +
                "INNER JOIN country ON daily_itinerary.country_id = country.id\n" +
                "INNER JOIN hotel ON daily_itinerary.hotel_id = hotel.id\n" +
                "INNER JOIN activity ON daily_itinerary.activity_id = activity.id where daily_itinerary.itinerary_id = ? ORDER BY day_number";
        return jdbcTemplate.query(sql, new DailyPlanDTOMapper(), itineraryId);
    }


    public ItineraryStep save(ItineraryStep itineraryStep) {
        String sql = "INSERT INTO itinerary_step (itinerary_id, city_id, country_id, hotel_id, position, day_number, description_per_day) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, itineraryStep.getItineraryId(), itineraryStep.getIdCity(), itineraryStep.getIdCountry(), itineraryStep.getIdHotel(), itineraryStep.getPosition(), itineraryStep.getDayNumber(), itineraryStep.getDescriptionPerDay());

        return itineraryStep;
    }



    public List<DailyPlanWithCityDto> findByItineraryIdWithCity(int itineraryId) {
        String sql = "SELECT " +
                "city.name AS cityName, " +
                "country.name AS countryName, " +
                "hotel.name AS hotelName, " +
                "hotel.description AS hotelDescription, " +
                "activity.name AS activityName, " +
                "activity.description AS activityDescription, " +
                "daily_itinerary.description_per_day, " +
                "daily_itinerary.day_number, " +
                "city.latitude, " +
                "city.longitude " +
                "FROM daily_itinerary " +
                "INNER JOIN city ON daily_itinerary.city_id = city.id " +
                "INNER JOIN country ON daily_itinerary.country_id = country.id " +
                "INNER JOIN hotel ON daily_itinerary.hotel_id = hotel.id " +
                "INNER JOIN activity ON daily_itinerary.activity_id = activity.id " +
                "WHERE daily_itinerary.itinerary_id = ? " +
                "ORDER BY day_number";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> DailyPlanWithCityDto.fromResultSet(rs), itineraryId);
    }


}
