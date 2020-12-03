package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta;

import java.util.List;

public interface ValutaService {

    ValutaDto findById(Integer id);

    List<ValutaDto> findAll();

    ValutaDto findByDrzaveJeDomovinaTrue();

    List<ValutaDto> findAll(String text, String... columns);
}
