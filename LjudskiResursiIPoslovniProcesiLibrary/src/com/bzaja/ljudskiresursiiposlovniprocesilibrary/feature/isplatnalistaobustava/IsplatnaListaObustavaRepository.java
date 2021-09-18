package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IsplatnaListaObustavaRepository extends CrudRepository<IsplatnaListaObustava, Integer> {

    List<IsplatnaListaObustava> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
