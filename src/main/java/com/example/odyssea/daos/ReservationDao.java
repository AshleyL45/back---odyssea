package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getInt("id"),
            rs.getInt("userId"),
            rs.getInt("itineraryId"),
            rs.getString("status"),
            rs.getDate("departureDate"),
            rs.getDate("returnDate"),
            rs.getDouble("totalPrice"),
            rs.getDate("purchaseDate"),
            rs.getInt("numberOfAdults"),
            rs.getInt("numberOfKids")
    );



    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }



    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reservation avec l'ID : " + id + " n'existe pas"));
    }



    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (userId, itineraryId, status, departureDate, returnDate, totalPrice, purchaseDate, numberOfAdults, numberOfKids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, reservation.getIdUser(), reservation.getIdItinerary(), reservation.getStatus(), reservation.getDepartureDate(), reservation.getReturnDate(), reservation.getTotalPrice(), reservation.getPurchase(), reservation.getNumberOfAdults(), reservation.getNumberOfKids());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        reservation.setIdReservation(id);
        return reservation;
    }



    public Reservation update(int id, Reservation reservation) {
        if (!reservationExists(id)) {
            throw new RuntimeException("Réservation avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE reservation SET userId = ?, itineraryId = ?, status = ?, departureDate = ?, returnDate = ?, totalPrice = ?, purchaseDate = ?, numberOfAdults = ?, numberOfKids = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, reservation.getIdUser(), reservation.getIdItinerary(), reservation.getStatus(), reservation.getDepartureDate(), reservation.getReturnDate(), reservation.getTotalPrice(), reservation.getPurchase(), reservation.getNumberOfAdults(), reservation.getNumberOfKids(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }



    private boolean reservationExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM product WHERE id_product = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }



    public boolean delete(int id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


}
