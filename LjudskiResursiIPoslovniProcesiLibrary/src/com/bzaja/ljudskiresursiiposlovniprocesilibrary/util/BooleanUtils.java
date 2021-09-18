package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

public final class BooleanUtils {

    private BooleanUtils() {

    }

    public static String toStringDaNe(Boolean b) {
        if (b == null) {
            return StringUtils.DEFAULT_EMPTY_VALUE;
        }

        return b ? "Da" : "Ne";
    }
}
