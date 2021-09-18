package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeUtils {

    private LocalDateTimeUtils() {

    }

    public static String format(LocalDateTime localDateTime, LocalDateTimePattern localDateTimePattern) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern(localDateTimePattern.getPattern())) : StringUtils.DEFAULT_EMPTY_VALUE;
    }
}
