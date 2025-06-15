package com.example.odyssea.daos.booking;

import com.example.odyssea.entities.booking.Booking;
import com.example.odyssea.enums.BookingStatus;
import com.example.odyssea.exceptions.BookingNotFoundException;
import com.example.odyssea.exceptions.DatabaseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class BookingDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("departure_date", "purchase_date");


    public BookingDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<Booking> booking = (rs, _) -> new Booking(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("itinerary_id"),
            rs.getString("status"),
            rs.getDate("departure_date").toLocalDate(),
            rs.getDate("return_date").toLocalDate(),
            rs.getBigDecimal("total_price"),
            rs.getDate("purchase_date").toLocalDate(),
            rs.getInt("number_of_adults"),
            rs.getInt("number_of_kids"),
            rs.getString("type")
    );


    // Récupère la liste de toutes les réservations
    public List<Booking> findAll() {
        String sql = "SELECT * FROM booking";
        return jdbcTemplate.query(sql, booking);
    }

    public List<Booking> getAllBookingsAndFilter(
            String status,
            String search,
            String sortField,
            String sortDirection
    ) {
        boolean hasStatus = status != null && !status.isBlank();
        boolean hasSearch = search != null && !search.isBlank();
        boolean hasValidSort = sortField != null && ALLOWED_SORT_FIELDS.contains(sortField);

        StringBuilder sql = new StringBuilder("SELECT r.* FROM booking r");
        Map<String, Object> params = new HashMap<>();

        if (hasSearch) {
            sql.append(" JOIN user u ON r.user_id = u.id");
        }

        sql.append(" WHERE 1=1");

        if (hasStatus) {
            sql.append(" AND r.status = :status");
            params.put("status", status);
        }

        if (hasSearch) {
            sql.append(" AND (LOWER(u.first_name) LIKE LOWER(:search) OR LOWER(u.last_name) LIKE LOWER(:search))");
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

        return namedParameterJdbcTemplate.query(sql.toString(), new MapSqlParameterSource(params), booking);
    }



    public Booking findByBookingId(int bookingId){
        String sql = "SELECT * FROM booking WHERE id = ?";
        return jdbcTemplate.query(sql, booking, bookingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking " + bookingId + "does not exist"));
    }

    // Récupère une réservation
    public Booking findById(int userId, int bookingId) {
        String sql = "SELECT * FROM booking WHERE user_id = ? AND id = ?";
        return jdbcTemplate.query(sql, booking, userId, bookingId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking " + bookingId + " of user with ID" + userId + " does not exist"));
    }

    //Récupère tous les itinéraires reservés d'un utilisateur
    public List<Booking> findAllUserBookings(int userId) {
        String sql = "SELECT * FROM booking WHERE booking.user_id = ?";
        return jdbcTemplate.query(sql, booking, userId);
    }


    // Enregistre une nouvelle réservation dans la base
    public Booking save(Booking booking) {
        String sql = """
        INSERT INTO booking
          (user_id,
           itinerary_id,
           status,
           departure_date,
           return_date,
           total_price,
           purchase_date,
           number_of_adults,
           number_of_kids,
           `type`)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        // Test basique : on fait juste l'update
        int rows = jdbcTemplate.update(sql,
                booking.getUserId(),
                booking.getItineraryId(),
                booking.getStatus(),
                Date.valueOf(booking.getDepartureDate()),
                Date.valueOf(booking.getReturnDate()),
                booking.getTotalPrice(),
                Date.valueOf(booking.getPurchaseDate()),
                booking.getNumberOfAdults(),
                booking.getNumberOfKids(),
                booking.getType()
        );

        if (rows != 1) {
            throw new DatabaseException("Expected 1 row inserted, got " + rows);
        }
        // On arrête là : pas de récupération de clé
        return booking;
    }



    // Met à jour une réservation existante identifiée par son ID
    public Booking update(int bookingId, Booking booking) {
        if (!booking(bookingId)) {
            throw new BookingNotFoundException("Booking " + bookingId + "does not exist");
        }

        String sql = "UPDATE booking SET user_id = ?, itinerary_id = ?, status = ?, departure_date = ?, return_date = ?, total_price = ?, purchase_date = ?, number_of_adults = ?, number_of_kids = ?, type = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                booking.getUserId(),
                booking.getItineraryId(),
                booking.getStatus(),
                booking.getDepartureDate(),
                booking.getReturnDate(),
                booking.getTotalPrice(),
                booking.getPurchaseDate(),
                booking.getNumberOfAdults(),
                booking.getNumberOfKids(),
                bookingId
        );

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update booking with booking ID :  " + bookingId);
        }

        return this.findByBookingId(bookingId);
    }

    // Changer le status d'une réservation
    public void updateBooking(int bookingId, BookingStatus status){
        if(!booking(bookingId)) {
            throw new BookingNotFoundException("Booking with ID " + bookingId + "does not exist");
        }

        String sql = "UPDATE booking SET status = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql, status.name(), bookingId);

        if(rowsAffected <= 0){
            throw new DatabaseException("Failed to update booking with ID : " + bookingId);
        }
    }

    // Changer le prix d'une réservation
    public void updateBooking(int bookingId, BigDecimal newPrice) {
        if (!booking(bookingId)) {
            throw new BookingNotFoundException("Booking with ID " + bookingId + " does not exist");
        }

        String sql = "UPDATE booking SET total_price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newPrice, bookingId);

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update total price for booking ID: " + bookingId);
        }
    }


    // Vérifie si une réservation existe dans la base
    private boolean booking(int bookingId) {
        String checkSql = "SELECT COUNT(*) FROM booking WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, bookingId);
        return count > 0;
    }

}
