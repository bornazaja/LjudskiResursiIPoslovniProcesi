package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IsplatnaListaOlaksicaRepository extends CrudRepository<IsplatnaListaOlaksica, Integer> {

    List<IsplatnaListaOlaksica> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
