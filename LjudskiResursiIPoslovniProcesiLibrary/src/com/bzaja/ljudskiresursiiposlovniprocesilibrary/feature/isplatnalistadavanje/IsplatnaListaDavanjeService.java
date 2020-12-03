package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje;

import java.util.List;

public interface IsplatnaListaDavanjeService {

    List<IsplatnaListaDavanjeDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
