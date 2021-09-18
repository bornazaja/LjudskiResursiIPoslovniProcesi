package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava;

import java.util.List;

public interface IsplatnaListaObustavaService {

    List<IsplatnaListaObustavaDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
