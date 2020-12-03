package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna;

public enum VrsteObracuna {

    OBRACUN_UGOVORA_O_RADU(1),
    OBRACUN_UGOVORA_O_DJELU(2),
    OBRACUN_STUDENTSKIH_UGOVORA(3);

    private Integer id;

    private VrsteObracuna(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
