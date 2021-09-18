package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.Descriptable;

public enum Operator implements Descriptable {

    AND("I"),
    OR("ILI");

    private String description;

    private Operator(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
