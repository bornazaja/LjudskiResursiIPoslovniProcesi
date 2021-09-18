package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora;

import java.util.List;

public interface VrstaUgovoraService {

    VrstaUgovoraDto findById(Integer id);

    List<VrstaUgovoraDto> findAll();
}
