package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna;

import java.util.List;

public interface VrstaObracunaService {

    VrstaObracunaDto findById(Integer id);

    List<VrstaObracunaDto> findAll();
}
