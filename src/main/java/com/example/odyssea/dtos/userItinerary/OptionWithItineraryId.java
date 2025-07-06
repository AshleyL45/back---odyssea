package com.example.odyssea.dtos.userItinerary;

import com.example.odyssea.entities.mainTables.Option;
import org.springframework.jdbc.core.RowMapper;

public class OptionWithItineraryId {

    private Integer itineraryId;
    private Option option;

    public OptionWithItineraryId() {
    }

    public OptionWithItineraryId(Integer itineraryId, Option option) {
        this.itineraryId = itineraryId;
        this.option = option;
    }

    public Integer getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Integer itineraryId) {
        this.itineraryId = itineraryId;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public static RowMapper<OptionWithItineraryId> rowMapper() {
        return (rs, rowNum) -> {
            Integer itineraryId = rs.getInt("user_itinerary_id");

            Option option = new Option(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("price"),
                    rs.getString("category")
            );

            return new OptionWithItineraryId(itineraryId, option);
        };
    }
}
