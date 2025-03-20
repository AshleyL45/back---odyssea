package com.example.odyssea.entities;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BlogArticle {

    private int id;

    @Min(value = 1, message = "Country ID must be greater than or equal to 1")
    private int countryId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "The introduction paragraph is required")
    @Size(max = 5000, message = "The introduction paragraph must not exceed 5000 characters")
    private String introParagraph;

    @NotBlank(message = "The 'when to go' paragraph is required")
    @Size(max = 5000, message = "The 'when to go' paragraph must not exceed 5000 characters")
    private String whenToGoParagraph;

    @NotBlank(message = "The 'what to see/do' paragraph is required")
    @Size(max = 5000, message = "The 'what to see/do' paragraph must not exceed 5000 characters")
    private String whatToSeeDoParagraph;

    @NotBlank(message = "The cultural advice paragraph is required")
    @Size(max = 5000, message = "The cultural advice paragraph must not exceed 5000 characters")
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
