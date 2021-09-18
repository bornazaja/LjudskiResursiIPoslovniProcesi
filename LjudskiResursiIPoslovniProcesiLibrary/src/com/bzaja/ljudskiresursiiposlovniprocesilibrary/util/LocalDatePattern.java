package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

public enum LocalDatePattern {

    HR("dd.MM.yyyy"),
    ISO_8601("yyyy-MM-dd");

    private String pattern;

    private LocalDatePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
