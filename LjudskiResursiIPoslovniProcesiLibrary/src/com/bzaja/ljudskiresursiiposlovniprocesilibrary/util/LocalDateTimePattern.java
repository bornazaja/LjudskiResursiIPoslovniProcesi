package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

public enum LocalDateTimePattern {

    HR("dd.MM.yyyy HH:mm:ss"),
    ISO_8601("yyyy-MM-dd HH:mm:ss");

    private String pattern;

    private LocalDateTimePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
