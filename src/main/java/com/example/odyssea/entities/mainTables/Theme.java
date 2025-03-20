package com.example.odyssea.entities.mainTables;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Theme {

    @Min(value = 1, message = "Theme ID must be greater than or equal to 1")
    private int themeId;

    @NotBlank(message = "Theme name is required")
    @Size(max = 255, message = "Theme name must not exceed 255 characters")
    private String themeName;

    public Theme() {}

    public Theme(int themeId, String themeName) {
        this.themeId = themeId;
        this.themeName = themeName;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
