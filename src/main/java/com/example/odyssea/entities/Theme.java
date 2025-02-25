package com.example.odyssea.entities;

public class Theme {


    private int idTheme;
    private int idItinerary;
    private String themeName;

    public Theme(){}
    public Theme(int idTheme, int idItinerary, String themeName){
        this.idTheme = idTheme;
        this.idItinerary = idItinerary;
        this.themeName = themeName;
    }


    public int getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }


    public int getIdItinerary() {
        return idItinerary;
    }


    public void setIdItinerary(int idItinerary) {
        this.idItinerary = idItinerary;
    }



    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
