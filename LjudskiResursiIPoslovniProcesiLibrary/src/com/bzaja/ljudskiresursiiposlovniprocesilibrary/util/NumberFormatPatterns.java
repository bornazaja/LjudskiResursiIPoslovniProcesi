package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

public enum NumberFormatPatterns {

    DECIMAL_HR("###,##0.00"),
    PERCENTAGE("0.00'%'");

    private String pattern;

    private NumberFormatPatterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
