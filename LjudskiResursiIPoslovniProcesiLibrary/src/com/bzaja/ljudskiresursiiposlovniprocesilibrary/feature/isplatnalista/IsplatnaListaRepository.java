package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IsplatnaListaRepository extends PagingAndSortingRepository<IsplatnaLista, Integer>, JpaSpecificationExecutor<IsplatnaLista> {

    List<IsplatnaLista> findAllByObracunUgovoraIdObracunUgovora(Integer idObracunUgovora);

    List<IsplatnaLista> findAllByUgovorZaposlenikIdZaposlenik(Integer idZaposlenik);
}
