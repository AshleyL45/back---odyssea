package com.example.odyssea.services;

import com.example.odyssea.daos.BlogArticleDao;
import com.example.odyssea.entities.mainTables.BlogArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogArticleService {

    private final BlogArticleDao blogArticleDao;

    @Autowired
    public BlogArticleService(BlogArticleDao blogArticleDao) {
        this.blogArticleDao = blogArticleDao;
    }

    /**
     * Récupère tous les articles de blog disponibles
     */
    public List<BlogArticle> getAllArticles() {
        return blogArticleDao.findAll();
    }

    /**
     * Récupère un article de blog spécifique par son identifiant
     */
    public BlogArticle getArticle(int id) {
        return blogArticleDao.findById(id).orElse(null);
    }

    /**
     * Crée un nouvel article de blog dans la base de données
     *
     * @param article L'article de blog à enregistrer
     */
    public void createArticle(BlogArticle article) {
        blogArticleDao.save(article);
    }

    /**
     * Met à jour un article de blog existant
     */
    public boolean updateArticle(int id, BlogArticle article) {
        if (blogArticleDao.findById(id).isEmpty()) {
            return false;
        }
        article.setId(id);
        blogArticleDao.update(article);
        return true;
    }

    /**
     * Supprime un article de blog par son identifiant
     */
    public boolean deleteArticle(int id) {
        if (blogArticleDao.findById(id).isEmpty()) {
            return false;
        }
        blogArticleDao.deleteById(id);
        return true;
    }
}
