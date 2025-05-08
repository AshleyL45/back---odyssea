// src/main/java/com/example/odyssea/daos/mainTables/ItineraryImageDao.java
package com.example.odyssea.daos.mainTables;

import com.example.odyssea.dtos.mainTables.ItineraryImageDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItineraryImageDao {
    private final JdbcTemplate jdbc;

    public ItineraryImageDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Récupère pour un itinéraire donné tous les liens image + rôle.
     */
    public List<ItineraryImageDto> findByItineraryId(int itineraryId) {
        String sql = """
            SELECT itineraryId, imageId, role
              FROM itineraryImages
             WHERE itineraryId = ?
            """;
        return jdbc.query(
                sql,
                (rs, rowNum) -> new ItineraryImageDto(
                        rs.getInt("itineraryId"),
                        rs.getInt("imageId"),
                        rs.getString("role")
                ),
                itineraryId
        );
    }

    /**
     * Liste tous les rôles disponibles pour un itinéraire.
     */
    public List<String> findRolesByItinerary(int itineraryId) {
        String sql = "SELECT role FROM itineraryImages WHERE itineraryId = ?";
        return jdbc.queryForList(sql, String.class, itineraryId);
    }
}
