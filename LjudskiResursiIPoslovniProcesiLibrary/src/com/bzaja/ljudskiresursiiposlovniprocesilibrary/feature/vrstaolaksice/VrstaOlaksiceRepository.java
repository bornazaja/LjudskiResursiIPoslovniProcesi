package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VrstaOlaksiceRepository extends PagingAndSortingRepository<VrstaOlaksice, Integer>, JpaSpecificationExecutor<VrstaOlaksice> {

}
