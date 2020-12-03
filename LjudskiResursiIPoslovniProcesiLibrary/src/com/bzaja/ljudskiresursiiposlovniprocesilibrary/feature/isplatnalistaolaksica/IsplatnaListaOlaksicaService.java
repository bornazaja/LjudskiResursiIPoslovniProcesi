package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica;

import java.util.List;

public interface IsplatnaListaOlaksicaService {

    List<IsplatnaListaOlaksicaDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
