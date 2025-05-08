// src/main/java/com/example/odyssea/daos/mainTables/ImageDao.java
package com.example.odyssea.daos.mainTables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Blob;

@Repository
public class ImageDao {
    private final JdbcTemplate jdbc;

    public ImageDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Récupère le BLOB de l’image associée à un imageId donné.
     */
    public byte[] findDataById(int imageId) {
        String sql = "SELECT data FROM images WHERE id = ?";
        return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> {
                    Blob blob = rs.getBlob("data");
                    return blob.getBytes(1, (int) blob.length());
                },
                imageId
        );
    }

    /**
     * (Optionnel) Récupère le BLOB de l’image pour un itinéraire et un rôle donnés.
     */
    public byte[] findDataByItineraryAndRole(int itineraryId, String role) {
        String sql = """
            SELECT i.data
              FROM images i
              JOIN itineraryImages ii ON ii.imageId = i.id
             WHERE ii.itineraryId = ?
               AND ii.role = ?
            """;
        return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> {
                    Blob blob = rs.getBlob("data");
                    return blob.getBytes(1, (int) blob.length());
                },
                itineraryId,
                role
        );
    }
}
