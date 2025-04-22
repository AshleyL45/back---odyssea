package com.example.odyssea.enums;

import com.example.odyssea.exceptions.ValidationException;

import java.util.Arrays;

public enum TripDuration {
    NINE(9, 1, 2, 6),
    SEVENTEEN(17, 2, 4, 12),
    TWENTY_FIVE(25, 3, 6, 18),
    THIRTY_THREE(33, 4, 8, 24);

    private final int days;
    private final int expectedCountries;
    private final int expectedCities;
    private final int expectedActivities;

    TripDuration(int days, int expectedCountries, int expectedCities, int expectedActivities) {
        this.days = days;
        this.expectedCountries = expectedCountries;
        this.expectedCities = expectedCities;
        this.expectedActivities = expectedActivities;
    }

    public int getExpectedCountries() {
        return expectedCountries;
    }

    public int getExpectedCities() {
        return expectedCities;
    }

    public int getExpectedActivities() {
        return expectedActivities;
    }

    public static TripDuration fromDays(int days) {
        return Arrays.stream(values())
                .filter(d -> d.days == days)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Invalid duration. The duration must be one of 9, 17, 25, or 33."));
    }
}

