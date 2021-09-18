package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos;

import java.util.List;

public interface RadniOdnosService {

    RadniOdnosDto findById(Integer id);

    List<RadniOdnosDto> findAll();
    
    List<RadniOdnosDto> findAll(String text, Integer idVrstaUgovora, String... columns);
}
