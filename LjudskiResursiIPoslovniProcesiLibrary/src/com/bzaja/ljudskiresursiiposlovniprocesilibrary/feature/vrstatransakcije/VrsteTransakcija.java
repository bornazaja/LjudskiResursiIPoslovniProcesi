package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije;

public enum VrsteTransakcija {

    PRIHOD(1),
    RASHOD(2);

    Integer id;

    private VrsteTransakcija(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
