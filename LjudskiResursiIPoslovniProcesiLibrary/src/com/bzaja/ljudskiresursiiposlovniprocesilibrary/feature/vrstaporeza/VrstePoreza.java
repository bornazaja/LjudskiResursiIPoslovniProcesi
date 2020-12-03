package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza;

public enum VrstePoreza {

    POREZ_NA_DOHODAK(1),
    POREZ_NA_DOBIT(2);

    private Integer id;

    private VrstePoreza(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
