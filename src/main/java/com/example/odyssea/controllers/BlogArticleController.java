package com.example.odyssea.controllers;

import com.example.odyssea.entities.mainTables.BlogArticle;
import com.example.odyssea.services.BlogArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog-articles")
public class BlogArticleController {

    private final BlogArticleService blogArticleService;

    @Autowired
    public BlogArticleController(BlogArticleService blogArticleService) {
        this.blogArticleService = blogArticleService;
    }

    /**
     * Récupère tous les articles de blog
     */
    @GetMapping
    public List<BlogArticle> getAllArticles() {
        return blogArticleService.getAllArticles();
    }

    /**
     * Récupère un article de blog spécifique par son identifiant
     */
    @GetMapping("/{id}")
    public BlogArticle getArticleById(@PathVariable int id) {
        return blogArticleService.getArticle(id);
    }

    /**
     * Crée un nouvel article de blog
     */
    @PostMapping
    public void createArticle(@RequestBody BlogArticle article) {
        blogArticleService.createArticle(article);
    }

    /**
     * Met à jour un article de blog existant
     */
    @PutMapping("/{id}")
    public boolean updateArticle(@PathVariable int id, @RequestBody BlogArticle article) {
        return blogArticleService.updateArticle(id, article);
    }

    /**
     * Supprime un article de blog par son identifiant
     */
    @DeleteMapping("/{id}")
    public boolean deleteArticle(@PathVariable int id) {
        return blogArticleService.deleteArticle(id);
    }
}
