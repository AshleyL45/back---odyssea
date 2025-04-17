package com.example.odyssea.utils;

import java.util.Random;

public class PriceUtils {
    private static final int MIN_TENS = 1;
    private static final int MAX_TENS = 10;
    private static final Random RANDOM = new Random();

    public static double generatePriceInTens() {
        int tens = RANDOM.nextInt(MAX_TENS - MIN_TENS + 1) + MIN_TENS;
        return tens * 10.0;
    }
}
