package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.MySelection;
import com.example.odyssea.entities.itinerary.Itinerary;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.SelectionAlreadyExistException;
import com.example.odyssea.exceptions.SelectionNotFoundException;
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

    // RowMapper pour convertir un enregistrement de la table mySelection en objet MySelection
    private final RowMapper<MySelection> mySelectionRowMapper = (rs, rowNum) -> new MySelection(
            rs.getInt("user_id"),
            rs.getInt("itinerary_id")
    );


    // Retourne tous les itinéraires de la séléction d'un user
    public List<Itinerary> findAllUserFavorites(int userId){
        String sql = "SELECT itinerary.* FROM my_selection\n" +
                "INNER JOIN itinerary ON my_selection.itinerary_id = itinerary.id WHERE my_selection.user_id = ?;";
        return jdbcTemplate.query(sql, (rs, _) -> new Itinerary(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("short_description"),
                rs.getInt("stock"),
                rs.getBigDecimal("price"),
                rs.getInt("total_duration"),
                rs.getInt("theme_id")
        ), userId);
    }

    // Retourne une sélection identifiée par le couple (userId, itineraryId)
    public MySelection findById(int userId, int itineraryId) {
        String sql = "SELECT * FROM my_selection WHERE user_id = ? AND itinerary_id = ?";
        return jdbcTemplate.query(sql, mySelectionRowMapper, userId, itineraryId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new SelectionNotFoundException("Selection with userId: " + userId + " and itineraryId: " + itineraryId + " does not exist"));
    }

    // Insère une nouvelle sélection dans la table mySelection
    public MySelection save(MySelection mySelection) {

        String checkSql = "SELECT COUNT(*) FROM my_selection WHERE user_id = ? AND itinerary_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class,
                mySelection.getUserId(), mySelection.getItineraryId());

        if (count != null && count > 0) {
            throw new SelectionAlreadyExistException("Selection already exists for userId: "
                    + mySelection.getUserId() + " and itinerary_id: " + mySelection.getItineraryId());
        }

        String insertSql = "INSERT INTO my_selection (user_id, itinerary_id) VALUES (?, ?)";
        jdbcTemplate.update(insertSql, mySelection.getUserId(), mySelection.getItineraryId());

        return mySelection;
    }

    // Met à jour une sélection identifiée par le couple (userId, itineraryId)
    public MySelection update(int userId, int itineraryId, MySelection mySelection) {
        // Vérifie d'abord que la sélection existe
        findById(userId, itineraryId);
        String sql = "UPDATE my_selection SET user_id = ?, itinerary_id = ? WHERE user_id = ? AND itinerary_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                mySelection.getUserId(),
                mySelection.getItineraryId(),
                userId,
                itineraryId
        );
        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update selection with userId: " + userId + " and itineraryId: " + itineraryId);
        }
        return findById(mySelection.getUserId(), mySelection.getItineraryId());
    }

    // Supprime une sélection identifiée par le couple (userId, itineraryId)
    public void delete(int userId, int itineraryId) {
        String sql = "DELETE FROM my_selection WHERE user_id = ? AND itinerary_id = ?";
        MySelection selection = findById(userId,itineraryId);

        int rowsAffected = jdbcTemplate.update(sql, userId, itineraryId);
        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to delete selection with userId: " + userId + " and itineraryId: " + itineraryId);
        }
    }
}
