/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource;

import com.bzaja.myjavafxlibrary.util.HTMLPathable;

/**
 *
 * @author Borna
 */
public enum HtmlFiles implements HTMLPathable {
    NAPREDNA_PRETRAGA("NaprednaPretraga.html");

    private String path;

    private HtmlFiles(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return "/com/bzaja/ljudskiresursiiposlovniprocesijavafx/content/html/" + path;
    }
}
