package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VrstaDodatkaRepository extends PagingAndSortingRepository<VrstaDodatka, Integer>, JpaSpecificationExecutor<VrstaDodatka> {

}
