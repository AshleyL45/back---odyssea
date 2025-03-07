package com.example.odyssea.controllers;

import com.example.odyssea.entities.mainTables.Theme;
import com.example.odyssea.services.ThemeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    // Endpoint pour récupérer la liste de tous les thèmes
    @GetMapping
    public ResponseEntity<List<Theme>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();
        return new ResponseEntity<>(themes, HttpStatus.OK);
    }

    // Endpoint pour récupérer un thème par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable int id) {
        Theme theme = themeService.getThemeById(id);
        return new ResponseEntity<>(theme, HttpStatus.OK);
    }

    // Endpoint pour créer un nouveau thème
    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme created = themeService.createTheme(theme);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Endpoint pour mettre à jour un thème existant identifié par son ID
    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable int id, @RequestBody Theme theme) {
        Theme updated = themeService.updateTheme(id, theme);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Endpoint pour supprimer un thème par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable int id) {
        boolean deleted = themeService.deleteTheme(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
