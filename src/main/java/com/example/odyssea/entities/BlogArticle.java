package com.example.odyssea.entities.mainTables;

public class BlogArticle {
    private int id;
    private int countryId;
    private String title;
    private String introParagraph;
    private String whenToGoParagraph;
    private String whatToSeeDoParagraph;
    private String culturalAdviceParagraph;

    public BlogArticle() {}

    public BlogArticle(int id, int countryId, String title, String introParagraph,
                       String whenToGoParagraph, String whatToSeeDoParagraph,
                       String culturalAdviceParagraph) {
        this.id = id;
        this.countryId = countryId;
        this.title = title;
        this.introParagraph = introParagraph;
        this.whenToGoParagraph = whenToGoParagraph;
        this.whatToSeeDoParagraph = whatToSeeDoParagraph;
        this.culturalAdviceParagraph = culturalAdviceParagraph;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCountryId() {
        return countryId;
    }
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIntroParagraph() {
        return introParagraph;
    }
    public void setIntroParagraph(String introParagraph) {
        this.introParagraph = introParagraph;
    }
    public String getWhenToGoParagraph() {
        return whenToGoParagraph;
    }
    public void setWhenToGoParagraph(String whenToGoParagraph) {
        this.whenToGoParagraph = whenToGoParagraph;
    }
    public String getWhatToSeeDoParagraph() {
        return whatToSeeDoParagraph;
    }
    public void setWhatToSeeDoParagraph(String whatToSeeDoParagraph) {
        this.whatToSeeDoParagraph = whatToSeeDoParagraph;
    }
    public String getCulturalAdviceParagraph() {
        return culturalAdviceParagraph;
    }
    public void setCulturalAdviceParagraph(String culturalAdviceParagraph) {
        this.culturalAdviceParagraph = culturalAdviceParagraph;
    }
}
