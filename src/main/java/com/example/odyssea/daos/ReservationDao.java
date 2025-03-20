package com.example.odyssea.daos;

import com.example.odyssea.dtos.reservation.ItineraryReservationDTO;
import com.example.odyssea.dtos.reservation.ReservationRecapDTO;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.exceptions.ReservationNotFoundException;
import com.example.odyssea.mapper.ReservationRecapDTOMapper;
import com.example.odyssea.mapper.ReservationRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDao {

    // JdbcTemplate permet d'accéder à la base de données
    private final JdbcTemplate jdbcTemplate;
    private final ItineraryDao itineraryDao;
    private final ReservationOptionDao reservationOptionDao;

    public ReservationDao(JdbcTemplate jdbcTemplate, ItineraryDao itineraryDao, ReservationOptionDao reservationOptionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.itineraryDao = itineraryDao;
        this.reservationOptionDao = reservationOptionDao;
    }


    // Récupère la liste de toutes les réservations
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, new ReservationRowMapper());
    }

    // Récupère une réservation
    public Reservation findById(int userId, int itineraryId) {
        String sql = "SELECT * FROM reservation WHERE userId = ? AND itineraryId = ?";
        return jdbcTemplate.query(sql, new ReservationRowMapper(), userId, itineraryId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Reservation of user " + userId + " with itinerary" + itineraryId + "does not exist"));
    }

    //Récupère tous les itinéraires reservés d'un utilisateur avec la date d'achat et le statut
    public List<ItineraryReservationDTO> findAllUserReservations(int userId){
        String sql = "SELECT itinerary.id, itinerary.name, itinerary.description, itinerary.shortDescription, " +
                "itinerary.price, itinerary.totalDuration, reservation.status, reservation.purchaseDate " +
                "FROM reservation " +
                "INNER JOIN itinerary ON reservation.itineraryId = itinerary.id " +
                "WHERE reservation.userId = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ItineraryReservationDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("shortDescription"),
                rs.getBigDecimal("price"),
                rs.getInt("totalDuration"),
                rs.getString("status"),
                rs.getDate("purchaseDate").toLocalDate()
        ), userId);
    }

    // Renvoie une réservation avec les details (les options et les vols)
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
                .orElseThrow(() -> new ReservationNotFoundException("Last done itinerary from user : " + userId + "with status" + status + "cannot be found."));
    }


    // Enregistre une nouvelle réservation dans la base
    public Reservation save(Reservation reservation) {
        // Insérer la réservation principale (sans les options)
        String sql = "INSERT INTO reservation (userId, itineraryId, status, departureDate, returnDate, totalPrice, purchaseDate, numberOfAdults, numberOfKids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                reservation.getUserId(),
                reservation.getItineraryId(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchaseDate(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids()
        );

        // Vérifier si optionIds est null ou vide
        if (reservation.getOptionIds() != null && !reservation.getOptionIds().isEmpty()) {
            // Une fois la réservation insérée, on récupère les userId et itineraryId pour les insérer dans la table de liaison
            for (Integer optionId : reservation.getOptionIds()) {
                reservationOptionDao.insertReservationOption(reservation.getUserId(), reservation.getItineraryId(), optionId);
            }
        }

        return reservation;
    }


    // Enregistre les options d'une réservation
    public void insertOptions(Reservation reservation) {
        for (Integer optionId : reservation.getOptionIds()) {
            reservationOptionDao.insertReservationOption(reservation.getUserId(), reservation.getItineraryId(), optionId);
        }
    }

    public Reservation getReservationWithOptions(int userId, int itineraryId) {
        // Récupérer la réservation
        String sqlReservation = "SELECT * FROM reservation WHERE userId = ? AND itineraryId = ?";
        Reservation reservation = jdbcTemplate.queryForObject(sqlReservation, new ReservationRowMapper(), userId, itineraryId);

        // Récupérer les options associées à cette réservation
        String sqlOptions = "SELECT option_id FROM reservationOption WHERE user_id = ? AND itinerary_id = ?";
        List<Integer> optionIds = jdbcTemplate.queryForList(sqlOptions, Integer.class, reservation.getUserId(), reservation.getItineraryId());

        // Ajouter les options à la réservation
        reservation.setOptionIds(optionIds);

        return reservation;
    }

    // Met à jour une réservation existante identifiée par son ID
    public Reservation update(int userId, int itineraryId, Reservation reservation) {
        if (!reservationExists(userId, itineraryId)) {
            throw new ReservationNotFoundException("Reservation of user " + userId + " with itinerary" + itineraryId + "does not exist");
        }

        String sql = "UPDATE reservation SET userId = ?, itineraryId = ?, status = ?, departureDate = ?, returnDate = ?, totalPrice = ?, purchaseDate = ?, numberOfAdults = ?, numberOfKids = ?, optionId = ? WHERE userId = ? AND itineraryId = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                reservation.getUserId(),
                reservation.getItineraryId(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchaseDate(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                reservation.getOptionIds(),
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
            throw new ReservationNotFoundException("Reservation of user " + userId + " with itinerary" + itineraryId + "does not exist");
        }

        String sql = "UPDATE reservation SET status = ? WHERE userId = ? AND itineraryId = ?";

        int rowsAffected = jdbcTemplate.update(sql, status, userId, itineraryId);

        if(rowsAffected > 0){
            return true;
        } else {
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
