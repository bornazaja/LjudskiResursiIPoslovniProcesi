package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.Descriptable;

public enum SearchOperation implements Descriptable{

    LESS_THAN("Manji"),
    LESS_THAN_OR_EQUAL_TO("Manji ili jednak"),
    EQUAL("Jednak"),
    STARTS_WITH("Počinje s"),
    CONTAINS("Sadržava"),
    ENDS_WITH("Završava s"),
    GREATER_THEN("Veći"),
    GREATER_THEN_OR_EQUAL_TO("Veći ili jednak");

    private String description;

    private SearchOperation(String description) {
        this.description = description;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
}
