package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class LocalDateUtils {

    private LocalDateUtils() {

    }

    public static String format(LocalDate localDate, LocalDatePattern localDatePattern) {
        return localDate != null ? localDate.format(DateTimeFormatter.ofPattern(localDatePattern.getPattern())) : StringUtils.DEFAULT_EMPTY_VALUE;
    }
}
