package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Reservation;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.ReservationNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("departureDate", "purchaseDate");


    public ReservationDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, _) -> new Reservation(
            rs.getInt("reservationId"),
            rs.getInt("userId"),
            rs.getInt("itineraryId"),
            rs.getString("status"),
            rs.getDate("departureDate").toLocalDate(),
            rs.getDate("returnDate").toLocalDate(),
            rs.getBigDecimal("totalPrice"),
            rs.getDate("purchaseDate").toLocalDate(),
            rs.getInt("numberOfAdults"),
            rs.getInt("numberOfKids"),
            rs.getString("type")
    );


    // Récupère la liste de toutes les réservations
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public List<Reservation> getAllBookingsAndFilter(
            String status,
            String search,
            String sortField,
            String sortDirection
    ) {
        boolean hasStatus = status != null && !status.isBlank();
        boolean hasSearch = search != null && !search.isBlank();
        boolean hasValidSort = sortField != null && ALLOWED_SORT_FIELDS.contains(sortField);

        StringBuilder sql = new StringBuilder("SELECT r.* FROM reservation r");
        Map<String, Object> params = new HashMap<>();

        if (hasSearch) {
            sql.append(" JOIN user u ON r.userId = u.id");
        }

        sql.append(" WHERE 1=1");

        if (hasStatus) {
            sql.append(" AND r.status = :status");
            params.put("status", status);
        }

        if (hasSearch) {
            sql.append(" AND (LOWER(u.firstName) LIKE LOWER(:search) OR LOWER(u.lastName) LIKE LOWER(:search))");
            params.put("search", "%" + search + "%");
        }

        if (hasValidSort) {
            sql.append(" ORDER BY r.").append(sortField);
            if ("desc".equalsIgnoreCase(sortDirection)) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }

        return namedParameterJdbcTemplate.query(sql.toString(), new MapSqlParameterSource(params), reservationRowMapper);
    }



    public Reservation findByBookingId(int bookingId){
        String sql = "SELECT * FROM reservation WHERE reservationId = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, bookingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Booking " + bookingId + "does not exist"));
    }

    // Récupère une réservation
    public Reservation findById(int userId, int bookingId) {
        String sql = "SELECT * FROM reservation WHERE userId = ? AND reservationId = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, userId, bookingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Booking " + bookingId + " of user with ID" + userId + " does not exist"));
    }

    //Récupère tous les itinéraires reservés d'un utilisateur
    public List<Reservation> findAllUserReservations(int userId) {
        String sql = "SELECT * FROM reservation WHERE reservation.userId = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, userId);
    }


    // Enregistre une nouvelle réservation dans la base
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (userId, itineraryId, status, departureDate, returnDate, totalPrice, purchaseDate, numberOfAdults, numberOfKids, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                reservation.getUserId(),
                reservation.getItineraryId(),
                reservation.getStatus(),
                reservation.getDepartureDate(),
                reservation.getReturnDate(),
                reservation.getTotalPrice(),
                reservation.getPurchaseDate(),
                reservation.getNumberOfAdults(),
                reservation.getNumberOfKids(),
                reservation.getType()
        );

        return reservation;
    }

    // Met à jour une réservation existante identifiée par son ID
    public Reservation update(int bookingId, Reservation reservation) {
        if (!reservationExists(bookingId)) {
            throw new ReservationNotFoundException("Booking " + bookingId + "does not exist");
        }

        String sql = "UPDATE reservation SET userId = ?, itineraryId = ?, status = ?, departureDate = ?, returnDate = ?, totalPrice = ?, purchaseDate = ?, numberOfAdults = ?, numberOfKids = ?, type = ? WHERE reservationId = ?";
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
                bookingId
        );

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update reservation with booking ID :  " + bookingId);
        }

        return this.findByBookingId(bookingId);
    }

    // Changer le status d'une réservation
    public void updateReservationStatus(int bookingId, BookingStatus status){
        if(!reservationExists(bookingId)) {
            throw new ReservationNotFoundException("Booking with ID " + bookingId + "does not exist");
        }

        String sql = "UPDATE reservation SET status = ? WHERE reservationId = ?";

        int rowsAffected = jdbcTemplate.update(sql, status.name(), bookingId);

        if(rowsAffected <= 0){
            throw new DatabaseException("Failed to update booking with ID : " + bookingId);
        }
    }

    // Changer le prix d'une réservation
    public void updateReservationPrice(int bookingId, BigDecimal newPrice) {
        if (!reservationExists(bookingId)) {
            throw new ReservationNotFoundException("Booking with ID " + bookingId + " does not exist");
        }

        String sql = "UPDATE reservation SET totalPrice = ? WHERE reservationId = ?";
        int rowsAffected = jdbcTemplate.update(sql, newPrice, bookingId);

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update total price for booking ID: " + bookingId);
        }
    }


    // Vérifie si une réservation existe dans la base
    private boolean reservationExists(int bookingId) {
        String checkSql = "SELECT COUNT(*) FROM reservation WHERE reservationId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, bookingId);
        return count > 0;
    }

}
