package com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource;

import com.bzaja.myjavafxlibrary.util.CssPathable;

public enum CssFiles implements CssPathable {
    DEFAULT("app.css"),
    MATERIAL_DESIGN("material-design.css");

    private String path;

    private CssFiles(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return "/com/bzaja/ljudskiresursiiposlovniprocesijavafx/content/css/" + path;
    }
}
