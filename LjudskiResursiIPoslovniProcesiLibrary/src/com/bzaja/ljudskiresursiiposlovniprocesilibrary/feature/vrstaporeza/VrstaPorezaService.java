package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza;

import java.util.List;

public interface VrstaPorezaService {

    VrstaPorezaDto findById(Integer id);

    List<VrstaPorezaDto> findAll();

    List<VrstaPorezaDto> findAll(String text, String... columns);
}
