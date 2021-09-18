package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.time.LocalDateTime;

public final class StringUtils {

    public static final String DEFAULT_EMPTY_VALUE = "-";

    private StringUtils() {

    }

    public static String generateFileNameWithCurrentDateTime(String fileName) {
        String currentDateTimeStr = LocalDateTimeUtils.format(LocalDateTime.now(), LocalDateTimePattern.HR);
        return String.format("%s_%s", fileName, currentDateTimeStr.replaceAll("[.:\\s]", "_"));
    }

    public static String generateFileNameWithCurrentDateTime(String fileName, String... values) {
        String valuesStr = String.join("_", values);
        String currentDateTimeStr = LocalDateTimeUtils.format(LocalDateTime.now(), LocalDateTimePattern.HR);
        return String.format("%s_%s_%s", fileName, valuesStr.replaceAll("\\s", "_"), currentDateTimeStr.replaceAll("[.:\\s]", "_"));
    }
}
