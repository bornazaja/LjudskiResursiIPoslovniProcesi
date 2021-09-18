package com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource;

public enum CssFiles {
    DEFAULT("/ljudskiresursiiposlovniprocesi.css");

    private String path;

    private CssFiles(String path) {
        this.path = path;
    }

    public String getPath() {
        return "/com/bzaja/ljudskiresursiiposlovniprocesijavafx/content/css" + path;
    }
}
