package com.example.odyssea.daos;

import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    // RowMapper pour convertir un enregistrement de la table myselection en objet MySelection
    private final RowMapper<MySelection> mySelectionRowMapper = (rs, rowNum) -> new MySelection(
            rs.getInt("userId"),
            rs.getInt("itineraryId")
    );

    // Retourne toutes les sélections de la table myselection
    public List<MySelection> findAll() {
        String sql = "SELECT * FROM mySelection";
        return jdbcTemplate.query(sql, mySelectionRowMapper);
    }

    // Retourne tous les itinéraires de la séléction d'un user
    public List<Itinerary> findAllUserFavorites(int userId){
        String sql = "SELECT itinerary.* FROM mySelection\n" +
                "INNER JOIN itinerary ON mySelection.itineraryId = itinerary.id WHERE mySelection.userId = ?;";
        return jdbcTemplate.query(sql, (rs, _) -> new Itinerary(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("shortDescription"),
                rs.getInt("stock"),
                rs.getBigDecimal("price"),
                rs.getInt("totalDuration"),
                rs.getInt("themeId")
        ), userId);
    }

    // Retourne une sélection identifiée par le couple (userId, itineraryId)
    public MySelection findById(int userId, int itineraryId) {
        String sql = "SELECT * FROM mySelection WHERE userId = ? AND itineraryId = ?";
        return jdbcTemplate.query(sql, mySelectionRowMapper, userId, itineraryId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Selection with userId: " + userId + " and itineraryId: " + itineraryId + " does not exist"));
    }

    // Insère une nouvelle sélection dans la table mySelection
    public MySelection save(MySelection mySelection) {
        String sql = "INSERT INTO mySelection (userId, itineraryId) VALUES (?, ?)";
        jdbcTemplate.update(sql, mySelection.getUserId(), mySelection.getItineraryId());
        return mySelection;
    }

    // Met à jour une sélection identifiée par le couple (userId, itineraryId)
    public MySelection update(int userId, int itineraryId, MySelection mySelection) {
        // Vérifie d'abord que la sélection existe
        findById(userId, itineraryId);
        String sql = "UPDATE mySelection SET userId = ?, itineraryId = ? WHERE userId = ? AND itineraryId = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                mySelection.getUserId(),
                mySelection.getItineraryId(),
                userId,
                itineraryId
        );
        if (rowsAffected <= 0) {
            throw new RuntimeException("Failed to update selection with userId: " + userId + " and itineraryId: " + itineraryId);
        }
        return findById(mySelection.getUserId(), mySelection.getItineraryId());
    }

    // Supprime une sélection identifiée par le couple (userId, itineraryId)
    public boolean delete(int userId, int itineraryId) {
        String sql = "DELETE FROM mySelection WHERE userId = ? AND itineraryId = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, itineraryId);
        return rowsAffected > 0;
    }
}
