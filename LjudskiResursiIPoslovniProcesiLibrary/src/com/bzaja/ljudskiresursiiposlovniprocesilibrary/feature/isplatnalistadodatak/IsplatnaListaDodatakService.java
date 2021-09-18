package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak;

import java.util.List;

public interface IsplatnaListaDodatakService {

    List<IsplatnaListaDodatakDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
