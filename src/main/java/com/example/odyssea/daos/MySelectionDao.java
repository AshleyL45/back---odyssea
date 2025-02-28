package com.example.odyssea.daos;

import com.example.odyssea.entities.MySelection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MySelectionDao {

    private final JdbcTemplate jdbcTemplate;

    public MySelectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<MySelection> mySelectionRowMapper = (rs, rowNum) -> new MySelection(
            rs.getInt("userId"),
            rs.getInt("itineraryId")
    );


    public List<MySelection> findAll() {
        String sql = "SELECT * FROM myselection";
        return jdbcTemplate.query(sql, mySelectionRowMapper);
    }



    public MySelection findById(int idUser) {
        String sql = "SELECT * FROM myselection WHERE userId = ?";
        return jdbcTemplate.query(sql, mySelectionRowMapper, idUser)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sélection avec l'ID de l'utilisateur : " + idUser + " n'existe pas"));
    }



    public MySelection save(MySelection mySelection) {
        String sql = "INSERT INTO myselection (userId, itineraryId) VALUES (?, ?)";
        jdbcTemplate.update(sql, mySelection.getIdUser(),mySelection.getIdItinerary());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        mySelection.setIdUser(id);
        return mySelection;
    }




    public MySelection update(int id, MySelection mySelection) {
        if (!mySelectionExists(id)) {
            throw new RuntimeException("Sélection avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE myselection SET userId = ?, itineraryId = ? WHERE userId = ?";
        int rowsAffected = jdbcTemplate.update(sql, mySelection.getIdUser(), mySelection.getIdItinerary(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }


    private boolean mySelectionExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM myselection WHERE userId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }




    public boolean delete(int id) {
        String sql = "DELETE FROM myselection WHERE itineraryId = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

}
