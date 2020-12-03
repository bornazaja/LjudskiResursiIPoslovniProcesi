package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola;

public enum Role {

    ADMINISTRATOR(1),
    OSOBLJE(2);

    private Integer id;

    private Role(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
