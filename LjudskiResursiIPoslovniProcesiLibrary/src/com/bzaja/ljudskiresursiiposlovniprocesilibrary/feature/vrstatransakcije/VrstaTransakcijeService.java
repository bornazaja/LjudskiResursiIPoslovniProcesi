package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije;

import java.util.List;

public interface VrstaTransakcijeService {

    VrstaTransakcijeDto findById(Integer id);

    List<VrstaTransakcijeDto> findAll();

    List<VrstaTransakcijeDto> findAll(String text, String... columns);
}
