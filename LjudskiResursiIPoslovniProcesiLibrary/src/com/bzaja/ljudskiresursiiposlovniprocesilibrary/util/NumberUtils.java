package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class NumberUtils {

    private NumberUtils() {

    }

    public static Integer getDefaultInt(String number, Integer defaultValue) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double getDefaultDouble(String number, Double defaultValue) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String format(Double value, NumberFormatPatterns numberFormatPatterns) {
        if (value == null) {
            return StringUtils.DEFAULT_EMPTY_VALUE;
        }

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat(numberFormatPatterns.getPattern(), decimalFormatSymbols);
        return decimalFormat.format(value);
    }

    public static boolean isPositive(Number value) {
        return value != null && value.doubleValue() > 0;
    }
}
