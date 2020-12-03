package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IsplatnaListaDodatakRepository extends CrudRepository<IsplatnaListaDodatak, Integer> {

    List<IsplatnaListaDodatak> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista);
}
