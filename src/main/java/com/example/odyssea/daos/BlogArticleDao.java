package com.example.odyssea.daos;

import com.example.odyssea.entities.BlogArticle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BlogArticleDao {

    private final JdbcTemplate jdbcTemplate;

    public BlogArticleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Récupérer un article par son ID
    public Optional<BlogArticle> findById(int id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new BlogArticleRowMapper())
                .stream().findFirst();
    }

    // Récupérer tous les articles
    public List<BlogArticle> findAll() {
        String sql = "SELECT * FROM article";
        return jdbcTemplate.query(sql, new BlogArticleRowMapper());
    }

    // Récupérer un article en fonction d’un pays
    public Optional<BlogArticle> findByCountryId(int countryId) {
        String sql = "SELECT * FROM article WHERE country_id = ?";
        return jdbcTemplate.query(sql, new Object[]{countryId}, new BlogArticleRowMapper())
                .stream()
                .findFirst();
    }

    // Ajouter un nouvel article
    public void save(BlogArticle article) {
        String sql = "INSERT INTO article (country_id, title, intro_paragraph, when_to_go_paragraph, what_to_see_do_paragraph, cultural_advice_paragraph) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, article.getCountryId(), article.getTitle(), article.getIntroParagraph(),
                article.getWhenToGoParagraph(), article.getWhatToSeeDoParagraph(), article.getCulturalAdviceParagraph());
    }

    // Mettre à jour un article existant
    public void update(BlogArticle article) {
        String sql = "UPDATE article SET title = ?, intro_paragraph = ?, when_to_go_paragraph = ?, what_to_see_do_paragraph = ?, cultural_advice_paragraph = ? WHERE id = ?";
        jdbcTemplate.update(sql, article.getTitle(), article.getIntroParagraph(), article.getWhenToGoParagraph(),
                article.getWhatToSeeDoParagraph(), article.getCulturalAdviceParagraph(), article.getId());
    }

    // Supprimer un article par son ID
    public void deleteById(int id) {
        String sql = "DELETE FROM article WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Mapper pour convertir un ResultSet en objet BlogArticle
    private static class BlogArticleRowMapper implements RowMapper<BlogArticle> {
        @Override
        public BlogArticle mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BlogArticle(
                    rs.getInt("id"),
                    rs.getInt("country_id"),
                    rs.getString("title"),
                    rs.getString("intro_paragraph"),
                    rs.getString("when_to_go_paragraph"),
                    rs.getString("what_to_see_do_paragraph"),
                    rs.getString("cultural_advice_paragraph")
            );
        }
    }
}