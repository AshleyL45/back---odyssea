package com.example.odyssea.daos.mainTables;

import com.example.odyssea.entities.mainTables.Image;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageDao {

    private final JdbcTemplate jdbcTemplate;

    public ImageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Image> imageRowMapper = (rs, rowNum) -> new Image(
            rs.getInt("id"),
            rs.getInt("entityId"),
            rs.getString("entityType"),
            rs.getString("sizeType"),
            rs.getString("link"),
            rs.getString("altText")
    );


    public List<Image> findAll() {
        String sql = "SELECT * FROM images";
        return jdbcTemplate.query(sql, imageRowMapper);
    }


    public List<Image> searchImage(String query){
        String sql = "SELECT * FROM images WHERE LOWER(entityType) LIKE LOWER(?) ";
        return jdbcTemplate.query(sql, imageRowMapper, "%" + query + "%");
    }



    public Image findById(int idImage) {
        String sql = "SELECT * FROM images WHERE id = ?";
        return jdbcTemplate.query(sql, imageRowMapper, idImage)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image avec l'ID : " + idImage + " n'existe pas"));
    }


    public Image save(Image image) {
        String sql = "INSERT INTO images (entityId, entityType, sizeType, link, altText) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, image.getIdEntity(), image.getEntityType(), image.getSizeType(), image.getLink(), image.getAltText());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        image.setIdImage(id);
        return image;
    }



    public Image update(int id, Image image) {
        if (!imageExists(id)) {
            throw new RuntimeException("Image avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE images SET entityId = ?, entityType = ?, sizeType = ?, link = ?, altText = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, image.getIdEntity(), image.getEntityType(), image.getSizeType(), image.getLink(), image.getAltText(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour de l'mage avec l'ID : " + id);
        }

        return this.findById(id);
    }



    private boolean imageExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM images WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }


    public boolean delete(int id) {
        String sql = "DELETE FROM images WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
