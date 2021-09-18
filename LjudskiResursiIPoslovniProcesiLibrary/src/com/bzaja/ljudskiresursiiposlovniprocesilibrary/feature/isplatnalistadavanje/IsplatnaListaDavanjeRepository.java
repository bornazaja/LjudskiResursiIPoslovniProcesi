package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IsplatnaListaDavanjeRepository extends CrudRepository<IsplatnaListaDavanje, Integer> {

    List<IsplatnaListaDavanje> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
