

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

    // Lit directement la colonne `data` (longblob) pour un id d'image
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

    // Lit la colonne `data` pour l'image liée à un itinéraire ET un rôle
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
