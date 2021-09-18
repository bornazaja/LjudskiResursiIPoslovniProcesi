package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VrstaObustaveRepository extends PagingAndSortingRepository<VrstaObustave, Integer>, JpaSpecificationExecutor<VrstaObustave> {

}
