package com.example.odyssea.mapper;

import com.example.odyssea.dtos.mainTables.DailyPlanDto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyPlanDTOMapper implements RowMapper<DailyPlanDto> {
    @Override
    public DailyPlanDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        DailyPlanDto dailyPlanDto = new DailyPlanDto();
        dailyPlanDto.setCityName(rs.getString("cityName"));
        dailyPlanDto.setCountryName(rs.getString("countryName"));
        dailyPlanDto.setHotelName(rs.getString("hotelName"));
        dailyPlanDto.setHotelDescription(rs.getString("hotelDescription"));
        dailyPlanDto.setActivityName(rs.getString("activityName"));
        dailyPlanDto.setActivityDescription(rs.getString("activityDescription"));
        dailyPlanDto.setDescriptionPerDay(rs.getString("description_per_day"));
        dailyPlanDto.setDayNumber(rs.getInt("day_number"));
        return dailyPlanDto;
    }
}
