package com.example.odyssea.mapper;

import com.example.odyssea.entities.mainTables.Reservation;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationRowMapper implements RowMapper<Reservation> {
    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        int userId = rs.getInt("userId");
        int itineraryId = rs.getInt("itineraryId");
        String status = rs.getString("status");


        LocalDate departureDate = rs.getDate("departureDate").toLocalDate();
        LocalDate returnDate = rs.getDate("returnDate").toLocalDate();


        BigDecimal totalPrice = rs.getBigDecimal("totalPrice");

        LocalDate purchaseDate = rs.getDate("purchaseDate").toLocalDate();

        int numberOfAdults = rs.getInt("numberOfAdults");
        int numberOfKids = rs.getInt("numberOfKids");

        Integer optionId = rs.getObject("optionId") != null ? rs.getInt("optionId") : null;

        return new Reservation(
                userId,
                itineraryId,
                status,
                departureDate,
                returnDate,
                totalPrice,
                purchaseDate,
                numberOfAdults,
                numberOfKids,
                null
        );
    }
}
