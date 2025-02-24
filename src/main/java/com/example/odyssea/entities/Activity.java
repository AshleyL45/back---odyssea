package com.example.odyssea.entities;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class Activity {

    private Long id;

    @NotNull(message = "La ville associée est obligatoire.")
    private Long cityId;

    @NotBlank(message = "Le nom ne doit pas être vide.")
    private String name;

    @NotBlank(message = "Le type d'activité ne doit pas être vide.")
    private String type;

    @Min(value = 1, message = "L'effort physique doit être au moins 1.")
    @Max(value = 10, message = "L'effort physique doit être au maximum 10.")
    private int physicalEffort;

    @Min(value = 1, message = "La durée doit être d'au moins 1 minute.")
    private int duration;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères.")
    private String description;

    @NotNull(message = "Le prix est obligatoire.")
    @PositiveOrZero(message = "Le prix doit être un nombre positif ou zéro.")
    private BigDecimal price;

    public Activity() {
    }

    public Activity(Long id, Long cityId, String name, String type, int physicalEffort,
                    int duration, String description, BigDecimal price) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.type = type;
        this.physicalEffort = physicalEffort;
        this.duration = duration;
        this.description = description;
        this.price = price;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getPhysicalEffort() { return physicalEffort; }
    public void setPhysicalEffort(int physicalEffort) { this.physicalEffort = physicalEffort; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
