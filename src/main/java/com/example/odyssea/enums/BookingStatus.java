package com.example.odyssea.enums;

public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED;

    public static boolean contains(String value) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
