package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol;

import java.util.List;

public interface SpolService {

    SpolDto findById(Integer id);

    List<SpolDto> findAll();

    List<SpolDto> findAll(String text, String... columns);
}
