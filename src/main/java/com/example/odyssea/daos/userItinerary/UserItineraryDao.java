package com.example.odyssea.daos.userItinerary;

import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.userItinerary.UserItinerary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Repository
public class UserItineraryDao {


    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(UserItinerary.class);


    public UserItineraryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserItinerary> userItineraryRowMapper = (rs, _) -> new UserItinerary(
            rs.getInt("id"),
            rs.getInt("userId"),
            rs.getDate("startDate"),
            rs.getDate("endDate"),
            rs.getBigDecimal("startingPrice"),
            rs.getInt("totalDuration"),
            rs.getString("departureCity"),
            rs.getString("itineraryName"),
            rs.getInt("numberOfAdults"),
            rs.getInt("numberOfKids")
    );

    /*public static UserItinerary fromEntity(UserItineraryDTO userItinerary) {
        return new UserItinerary (
                userItinerary.getUserId(),
                userItinerary.getStartDate(),
                userItinerary.getDepartureCity(),
                userItinerary.getEndDate(),
                userItinerary.getTotalDuration(),
                userItinerary.getStartingPrice()
        );
    }*/

    public List<UserItinerary> findAll (){
        String sql = "SELECT * FROM userItinerary";
        return jdbcTemplate.query(sql, userItineraryRowMapper);
    }

    public UserItinerary findById(int id){
        String sql = "SELECT * FROM userItinerary WHERE id = ?";
        return jdbcTemplate.query(sql, userItineraryRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The user itinerary id " + id + "you are looking for does not exist."));
    }

    public List<UserItinerary> findAllUserItineraries(int userId){
        String sql = "SELECT * FROM userItinerary WHERE userId = ?";
        return jdbcTemplate.query(sql, userItineraryRowMapper, userId);
    }

    public UserItinerary save(UserItinerary userItinerary) throws Exception {

        try {
            String sql = "INSERT INTO userItinerary (userId, startDate, endDate, startingPrice, totalDuration, departureCity, itineraryName, numberOfAdults, numberOfKids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            log.info("Insertion des données : userId={}, startDate={}, endDate={}, startingPrice={}, totalDuration={}, departureCity={}, itineraryName={}, numberOfAdults={}, numberOfKids={}",
                    userItinerary.getUserId(), userItinerary.getStartDate(), userItinerary.getEndDate(),
                    userItinerary.getStartingPrice(), userItinerary.getTotalDuration(), userItinerary.getDepartureCity(),
                    userItinerary.getItineraryName(), userItinerary.getNumberOfAdults(), userItinerary.getNumberOfKids());
             jdbcTemplate.update(sql, userItinerary.getUserId(), userItinerary.getStartDate(), userItinerary.getEndDate(), userItinerary.getStartingPrice(), userItinerary.getTotalDuration(), userItinerary.getDepartureCity(), userItinerary.getItineraryName(), userItinerary.getNumberOfAdults(), userItinerary.getNumberOfKids());
        } catch (Exception e) {
            log.info(e.getMessage().toUpperCase());
            throw new Exception(e);
        }
            String sqlGetId = "SELECT LAST_INSERT_ID()";
            Integer id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);
            if (id == null) {
                log.error("Erreur lors de la récupération de l'ID de l'insertion.");
            } else {
                log.info("ID inséré : {}", id);
            }

            userItinerary.setId(id);
            return userItinerary;

    }

    public UserItinerary update(int id, UserItinerary userItinerary){
        if(!userItineraryExists(id)){
            throw new RuntimeException("The user itinerary you are looking for does not exist.");
        }

        String sql = "UPDATE userItinerary SET userId = ?, startDate = ?, endDate = ?, startingPrice = ?, totalDuration = ?, departureCity = ?, itineraryName = ?, numberOfAdults = ?, numberOfKids = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userItinerary.getUserId(), userItinerary.getStartDate(), userItinerary.getEndDate(), userItinerary.getTotalDuration(), userItinerary.getDepartureCity(), userItinerary.getItineraryName(), userItinerary.getNumberOfAdults(), userItinerary.getNumberOfKids(), id);

        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary with id : " + id);
        }

        return this.findById(id);

    }

    public boolean updateUserItineraryName(int id, String newItineraryName){
        if(!userItineraryExists(id)){
            throw new RuntimeException("The user itinerary you are looking for does not exist.");
        }

        String sql = "UPDATE userItinerary SET itineraryName = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql, newItineraryName, id);
        if(rowsAffected <= 0){
            throw new RuntimeException("Failed to update the user itinerary with id : " + id);
        }

        return true;
    }

    /*public List<Option> getOptions (int userItineraryId){ // Gets options of a userItinerary
        String sql = "SELECT options.* FROM userItinerary \n" +
                "INNER JOIN options ON userItinerary.optionId = options.id WHERE userItinerary.id = ?";
        return jdbcTemplate.query(sql, new Object[]{userItineraryId}, new BeanPropertyRowMapper<>(Option.class));
    }*/

    /*public List<Flight> getFlights (int userItineraryId){ // Gets flights of a userItinerary
        String sql = "SELECT flight.* FROM userItinerary \n" +
                "INNER JOIN flight ON userItinerary.flightId = flight.id WHERE userItinerary.id = ?";
        return jdbcTemplate.query(sql, new Object[]{userItineraryId}, new BeanPropertyRowMapper<>(Flight.class));
    }*/

    public boolean deleteUserItinerary (int id){ // Deletes all information related with this user itinerary
        String sql = "DELETE FROM userItinerary WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        return rowsAffected > 0;
    }

    public boolean userItineraryExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM userItinerary WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }
}
