package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UgovorODjeluRepository extends PagingAndSortingRepository<UgovorODjelu, Integer>, JpaSpecificationExecutor<UgovorODjelu> {

    List<UgovorODjelu> findByIdUgovorIn(List<Integer> ids);

    List<UgovorODjelu> findByIsplatneListeObracunUgovoraIdObracunUgovora(Integer idObracunUgovora);
}
