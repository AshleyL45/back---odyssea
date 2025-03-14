package com.example.odyssea.mapper;

import com.example.odyssea.dtos.ReservationRecapDTO;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationRecapDTOMapper implements RowMapper<ReservationRecapDTO> {

    @Override
    public ReservationRecapDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

        ReservationRecapDTO recap = new ReservationRecapDTO();
        recap.setIdUser(rs.getInt("userId"));
        recap.setIdItinerary(rs.getInt("itineraryId"));
        recap.setStatus(rs.getString("status"));
        recap.setDepartureDate(rs.getDate("departureDate"));
        recap.setReturnDate(rs.getDate("returnDate"));
        recap.setTotalPrice(rs.getBigDecimal("totalPrice"));
        recap.setPurchaseDate(rs.getDate("purchaseDate"));
        recap.setNumberOfAdults(rs.getInt("numberOfAdults"));
        recap.setNumberOfKids(rs.getInt("numberOfKids"));


        List<Option> options = new ArrayList<>();
        do {
            Option option = new Option();
            option.setId(rs.getInt("options.id"));
            option.setName(rs.getString("options.name"));
            options.add(option);
        } while (rs.next() && rs.getInt("userId") == recap.getIdUser());

        recap.setOptions(options);

        return recap;
    }
}

