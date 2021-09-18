package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora;

public enum VrsteUgovora {

    UGOVOR_O_RADU(1),
    UGOVOR_O_DJELU(2),
    STUDENTSKI_UGOVOR(3);

    private Integer id;

    private VrsteUgovora(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
