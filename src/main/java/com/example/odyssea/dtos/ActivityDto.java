package com.example.odyssea.dtos;

import com.example.odyssea.entities.mainTables.Activity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityDto {
    private String name;
    private String description;
    // Format ISO-8601 (ex. "PT90M")
    private String minimumDuration;
    // Contient par exemple { "amount": 25.0 }
    private Map<String, Object> price;

    public ActivityDto() {}

    public ActivityDto(String name, String description, String minimumDuration, Map<String, Object> price) {
        this.name = name;
        this.description = description;
        this.minimumDuration = minimumDuration;
        this.price = price;
    }

    public static ActivityDto fromEntity(Activity activity) {
        Map<String, Object> priceMap = new HashMap<>();
        priceMap.put("amount", activity.getPrice());

        return new ActivityDto (
                activity.getName(),
                activity.getDescription(),
                String.valueOf(activity.getDuration()),
                priceMap
        );
    }

    /**
     * Convertit le DTO en entité Activity tout en gérant la conversion de durée
     */
    public Activity toActivity(int cityId) {
        if (this.name == null || this.name.isEmpty()) {
            throw new IllegalArgumentException("The 'name' field is mandatory!");
        }

        LocalTime durationTime = parseDuration(this.minimumDuration);
        //int duration = parseDuration(this.minimumDuration);
        Double parsedPrice = parsePrice(this.price);
        // Utiliser une valeur par défaut si description est null
        String descriptionNonNull = (this.description != null ? this.description : "No description available");

        return new Activity(0, cityId, this.name, "Other", "Low", durationTime, descriptionNonNull, parsedPrice);
    }


    /**
     * Convertit une durée au format ISO-8601 en minutes
     */
    private LocalTime parseDuration(String isoDuration) {
        if (isoDuration != null && !isoDuration.isEmpty()) {
            try {
                Duration duration = Duration.parse(isoDuration);
                return LocalTime.ofSecondOfDay(duration.toSeconds()); // Convertir en HH:mm:ss
            } catch (Exception e) {
                System.out.println("[ActivityDto] Time conversion error: " + e.getMessage());
            }
        }
        return LocalTime.MIDNIGHT;
    }

    /**
     * Convertit le prix sous forme de Map en Double
     */
    private Double parsePrice(Map<String, Object> price) {
        if (price != null && price.containsKey("amount")) {
            try {
                return Double.parseDouble(price.get("amount").toString());
            } catch (NumberFormatException e) {
                System.out.println("[ActivityDto] Price conversion error: " + e.getMessage());
            }
        }
        return 0.0;
    }

    /**
     * Convertit un nombre de minutes en format TIME (HH:MM:00)
     */
    public static String convertMinutesToTime(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return String.format("%02d:%02d:00", hours, remainingMinutes);
    }

    /**
     * Convertit un format TIME (HH:MM:00) en minutes
     */
    public static int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMinimumDuration() {
        return minimumDuration;
    }
    public void setMinimumDuration(String minimumDuration) {
        this.minimumDuration = minimumDuration;
    }
    public Map<String, Object> getPrice() {
        return price;
    }
    public void setPrice(Map<String, Object> price) {
        this.price = price;
    }
}
