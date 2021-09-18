/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.Descriptable;

/**
 *
 * @author Borna
 */
public enum SearchCriteriaType implements Descriptable {
    INCLUDE("Uključiv"),
    EXCLUDE("Nije uključiv");

    private String description;

    private SearchCriteriaType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
