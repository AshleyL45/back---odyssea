package com.example.odyssea.daos;

import com.example.odyssea.entities.mainTables.Option;
import com.example.odyssea.exceptions.ResourceNotFoundException;
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
            rs.getBigDecimal("price")
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
                .orElseThrow(() -> new ResourceNotFoundException("The option you are looking for does not exist."));
    }

    public Option findPrice (int optionId){
        String sql = "SELECT name, description, price FROM options WHERE id = ?";
        return jdbcTemplate.query(sql, optionRowMapper, optionId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The option you are looking for does not exist."));
    }

    public Option save (Option option){
        String sql = "INSERT INTO options (name, description, price) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, option.getName(), option.getDescription(), option.getPrice());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        option.setId(id);
        return option;
    }

    public Option update(int id, Option option){
        if(!optionExists(id)){
            throw new ResourceNotFoundException("The option you are looking for does not exist.");
        }

        String sql = "UPDATE options SET name = ?, description = ?, price = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, option.getName(), option.getDescription(), option.getPrice(), id);

        if(rowsAffected <= 0){
            throw new ResourceNotFoundException("Failed to update the option with id : " + id);
        }

        return this.findById(id);
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM options WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean optionExists(int id){
        String sqlCheck = "SELECT COUNT(*) FROM options WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        return count > 0;
    }
}
