package com.example.odyssea.daos;

import com.example.odyssea.dtos.ItineraryReservationDTO;
import com.example.odyssea.dtos.ReservationRecapDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.mapper.ReservationRecapDTOMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationDao {

    // JdbcTemplate permet d'accéder à la base de données
    private final JdbcTemplate jdbcTemplate;
    private final ItineraryDao itineraryDao;

    public ReservationDao(JdbcTemplate jdbcTemplate, ItineraryDao itineraryDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.itineraryDao = itineraryDao;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getInt("userId"),
            rs.getInt("itineraryId"),
            rs.getString("status"),
            rs.getDate("departureDate"),
            rs.getDate("returnDate"),
            rs.getDouble("totalPrice"),
            rs.getDate("purchaseDate"),
            rs.getInt("numberOfAdults"),
            rs.getInt("numberOfKids"),
            rs.getInt("optionId"),
            rs.getInt("planeRideId")
    );


    // Récupère la liste de toutes les réservations
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    // Récupère une réservation
    public Reservation findById(int userId, int itineraryId) {
        String sql = "SELECT * FROM reservation WHERE userId = ? AND itineraryId = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, userId, itineraryId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reservation of user " + userId + " with itinerary" + itineraryId + "does not exist"));
    }

    //Récupère tous les itinéraires reservés d'un utilisateur avec la date d'achat et le statut
    public List<ItineraryReservationDTO> findAllUserReservations(int userId){
        String sql = "SELECT itinerary.*, reservation.purchaseDate, reservation.status FROM reservation\n" +
                "INNER JOIN itinerary ON reservation.itineraryId = itinerary.id WHERE reservation.userId = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItineraryReservationDTO.class), userId);
    }

    // Renvoie une réservation aveinic les details (les options et les vols)
    public ReservationRecapDTO findReservationDetails(int userId, int itineraryId) {
        String sql = "SELECT reservation.*, options.*, planeRide.* " +
                "FROM reservation " +
                "INNER JOIN options ON reservation.optionId = options.id " +
                "INNER JOIN planeRide ON reservation.planeRideId = planeRide.id " +
                "WHERE reservation.userId = ? AND reservation.itineraryId = ?";

        return jdbcTemplate.queryForObject(sql, new ReservationRecapDTOMapper(), userId, itineraryId);
    }


    // Récupère le dernier itinéraire en statut "confirmé"
    public Itinerary findLastDoneItinerary(int userId, String status) {
        String sql = "SELECT itinerary.* FROM reservation\n" +
                "                INNER JOIN itinerary ON reservation.itineraryId = itinerary.id\n" +
                "                WHERE reservation.userId = ? AND reservation.status = ? AND reservation.returnDate < CURDATE() \n" +
                "                ORDER BY reservation.purchaseDate DESC LIMIT 1;";

        return jdbcTemplate.query(sql, itineraryDao.itineraryRowMapper, userId, status)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Last done itinerary from user : " + userId + "with status" + status + "cannot be found."));
    }


    // Enregistre une nouvelle réservation dans la base
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (userId, itineraryId, status, departureDate, returnDate, totalPrice, purchaseDate, numberOfAdults, numberOfKids, optionId, planeRideId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reservation.getIdUser(),
                reservation.getIdItinerary(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchase(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                reservation.getOptionId(),
                reservation.getPlaneRideId()
        );

        return reservation;
    }

    // Met à jour une réservation existante identifiée par son ID
    public Reservation update(int userId, int itineraryId, Reservation reservation) {
        if (!reservationExists(userId, itineraryId)) {
            throw new RuntimeException("Reservation of user " + userId + " with itinerary" + itineraryId + "does not exist");
        }

        String sql = "UPDATE reservation SET userId = ?, itineraryId = ?, status = ?, departureDate = ?, returnDate = ?, totalPrice = ?, purchaseDate = ?, numberOfAdults = ?, numberOfKids = ? WHERE userId = ? AND itineraryId = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                reservation.getIdUser(),
                reservation.getIdItinerary(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchase(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                reservation.getOptionId(),
                reservation.getPlaneRideId(),
                userId,
                itineraryId
        );

        if (rowsAffected <= 0) {
            throw new RuntimeException("Failed to update reservation with user ID :  " + userId + "with itinerary ID : " + itineraryId);
        }

        return this.findById(userId, itineraryId);
    }

    // Changer le status d'une réservation
    public boolean updateReservationStatus(int userId, int itineraryId, String status){
        if(!reservationExists(userId, itineraryId)) {
            throw new RuntimeException("Reservation of user " + userId + " with itinerary" + itineraryId + "does not exist");
        }

        String sql = "UPDATE reservation SET status = ? WHERE userId = ? AND itineraryId = ?";

        int rowsAffected = jdbcTemplate.update(sql, status, userId, itineraryId);

        if(rowsAffected > 0){
            return true;
        }else {
            throw new RuntimeException("Failed to update reservation with user ID :  " + userId + "with itinerary ID : " + itineraryId);
        }
    }

    // Vérifie si une réservation existe dans la base
    private boolean reservationExists(int userId, int itineraryId) {
        String checkSql = "SELECT COUNT(*) FROM reservation WHERE userId = ? AND itineraryId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, itineraryId);
        return count > 0;
    }

    // Supprime une réservation par son identifiant
    public boolean delete(int userId, int itineraryId) {
        String sql = "DELETE FROM reservation WHERE userId = ? AND itineraryId = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, itineraryId);
        return rowsAffected > 0;
    }
}
