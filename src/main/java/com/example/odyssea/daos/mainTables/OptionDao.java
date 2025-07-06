package com.example.odyssea.daos.mainTables;
import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.exceptions.OptionNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OptionDao {
    private final JdbcTemplate jdbcTemplate;

    public OptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Option> optionRowMapper = (rs, _) -> new Option(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getString("category")
    );

    public List<Option> findAll (){
        String sql = "SELECT * FROM options";
        return jdbcTemplate.query(sql, optionRowMapper);
    }

    public Option findById(int optionId){
        String sql = "SELECT * FROM options WHERE id = ?";
        return jdbcTemplate.query(sql, optionRowMapper, optionId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OptionNotFound("The option you are looking for does not exist."));
    }

}
