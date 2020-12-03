package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VrstaDavanjaRepository extends PagingAndSortingRepository<VrstaDavanja, Integer>, JpaSpecificationExecutor<VrstaDavanja> {

}
