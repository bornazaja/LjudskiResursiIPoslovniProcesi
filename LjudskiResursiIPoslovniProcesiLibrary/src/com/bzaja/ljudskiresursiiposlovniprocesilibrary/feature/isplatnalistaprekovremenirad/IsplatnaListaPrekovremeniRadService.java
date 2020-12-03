package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad;

import java.util.List;

public interface IsplatnaListaPrekovremeniRadService {

    List<IsplatnaListaPrekovremeniRadDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
