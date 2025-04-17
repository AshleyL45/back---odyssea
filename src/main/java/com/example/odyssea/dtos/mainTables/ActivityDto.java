package com.example.odyssea.dtos.mainTables;

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

    public Activity toActivity(int cityId) {
        if (this.name == null || this.name.isEmpty()) {
            throw new IllegalArgumentException("The 'name' field is mandatory!");
        }

        LocalTime durationTime = parseDuration(this.minimumDuration);
        Double parsedPrice = parsePrice(this.price);
        String descriptionNonNull = (this.description != null ? this.description : "No description available");

        return new Activity(0, cityId, this.name, "Other", "Low", durationTime, descriptionNonNull, parsedPrice);
    }

    private LocalTime parseDuration(String isoDuration) {
        if (isoDuration != null && !isoDuration.isEmpty()) {
            try {
                Duration duration = Duration.parse(isoDuration);
                return LocalTime.ofSecondOfDay(duration.toSeconds());
            } catch (Exception e) {
                System.out.println("[ActivityDto] Time conversion error: " + e.getMessage());
            }
        }
        return LocalTime.MIDNIGHT;
    }

    private static final double DEFAULT_PRICE = 0.0;

    private Double parsePrice(Map<String, Object> price) {
        if (price != null && price.get("amount") != null) {
            try {
                return Double.parseDouble(price.get("amount").toString());
            } catch (NumberFormatException e) {
                System.out.println("[ActivityDto] Price conversion error: " + e.getMessage());
            }
        }
        return 0.0;
    }

    public static String convertMinutesToTime(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return String.format("%02d:%02d:00", hours, remainingMinutes);
    }

    public static int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

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
