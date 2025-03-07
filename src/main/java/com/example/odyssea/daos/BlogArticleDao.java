package com.example.odyssea.daos;

import com.example.odyssea.entities.BlogArticle;
import com.example.odyssea.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public BlogArticleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Enregistre un nouvel article de blog dans la base de données
     */
    public void save(BlogArticle article) {
        String sql = "INSERT INTO article (countryId, title, intro_paragraph, when_to_go_paragraph, " +
                "what_to_see_do_paragraph, cultural_advice_paragraph) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                article.getCountryId(),
                article.getTitle(),
                article.getIntroParagraph(),
                article.getWhenToGoParagraph(),
                article.getWhatToSeeDoParagraph(),
                article.getCulturalAdviceParagraph());
    }

    /**
     * Met à jour un article existant
     */
    public void update(BlogArticle article) {
        String sql = "UPDATE article SET countryId = ?, title = ?, intro_paragraph = ?, when_to_go_paragraph = ?, " +
                "what_to_see_do_paragraph = ?, cultural_advice_paragraph = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                article.getCountryId(),
                article.getTitle(),
                article.getIntroParagraph(),
                article.getWhenToGoParagraph(),
                article.getWhatToSeeDoParagraph(),
                article.getCulturalAdviceParagraph(),
                article.getId());
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Blog article with id " + article.getId() + " not found for update.");
        }
    }

    /**
     * Supprime un article par son identifiant
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM article WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Blog article with id " + id + " not found for deletion.");
        }
    }

    /**
     * Recherche un article par son identifiant
     */
    public Optional<BlogArticle> findById(int id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        List<BlogArticle> articles = jdbcTemplate.query(sql, new BlogArticleRowMapper(), id);
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("Blog article with id " + id + " not found.");
        }
        return Optional.of(articles.get(0));
    }

    /**
     * Récupère tous les articles
     */
    public List<BlogArticle> findAll() {
        String sql = "SELECT * FROM article";
        return jdbcTemplate.query(sql, new BlogArticleRowMapper());
    }

    /**
     * Recherche des articles en fonction du countryId
     */
    public List<BlogArticle> findByCountryId(int countryId) {
        String sql = "SELECT * FROM article WHERE countryId = ?";
        List<BlogArticle> articles = jdbcTemplate.query(sql, new BlogArticleRowMapper(), countryId);
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("No blog articles found for country id " + countryId);
        }
        return articles;
    }

    /**
     * RowMapper pour transformer un ResultSet en objet BlogArticle
     */
    private static class BlogArticleRowMapper implements RowMapper<BlogArticle> {
        @Override
        public BlogArticle mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BlogArticle(
                    rs.getInt("id"),
                    rs.getInt("countryId"),
                    rs.getString("title"),
                    rs.getString("intro_paragraph"),
                    rs.getString("when_to_go_paragraph"),
                    rs.getString("what_to_see_do_paragraph"),
                    rs.getString("cultural_advice_paragraph")
            );
        }
    }
}
