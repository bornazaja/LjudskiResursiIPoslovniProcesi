/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource;

/**
 *
 * @author Borna
 */
public enum HtmlFiles {
    UPUTE_ZA_PRETRAZIVANJE_I_SORTIRANJE("/UputeZaPretrazivanjeISortiranje.html");

    private String path;

    private HtmlFiles(String path) {
        this.path = path;
    }

    public String getPath() {
        return "/com/bzaja/ljudskiresursiiposlovniprocesijavafx/content/html" + path;
    }
}
