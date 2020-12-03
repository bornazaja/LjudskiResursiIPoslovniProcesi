package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IsplatnaListaPrekovremeniRadRepository extends CrudRepository<IsplatnaListaPrekovremeniRad, Integer> {

    List<IsplatnaListaPrekovremeniRad> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
