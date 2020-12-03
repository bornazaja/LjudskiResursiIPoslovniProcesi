package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola;

import java.util.List;

public interface RolaService {

    RolaDto findById(Integer id);

    List<RolaDto> findAll();

    List<RolaDto> findAll(String text, String... columns);
}
