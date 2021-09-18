package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VrstaPorezaRepository extends PagingAndSortingRepository<VrstaPoreza, Integer>, JpaSpecificationExecutor<VrstaPoreza> {

}
