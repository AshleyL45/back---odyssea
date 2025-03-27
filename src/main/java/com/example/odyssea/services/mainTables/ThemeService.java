package com.example.odyssea.services.mainTables;

import com.example.odyssea.daos.mainTables.ThemeDao;
import com.example.odyssea.entities.mainTables.Theme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> getAllThemes() {
        return themeDao.findAll();
    }

    public Theme getThemeById(int id) {
        return themeDao.findById(id);
    }

    public Theme createTheme(Theme theme) {
        return themeDao.save(theme);
    }

    public Theme updateTheme(int id, Theme theme) {
        return themeDao.update(id, theme);
    }

    public boolean deleteTheme(int id) {
        return themeDao.delete(id);
    }
}
