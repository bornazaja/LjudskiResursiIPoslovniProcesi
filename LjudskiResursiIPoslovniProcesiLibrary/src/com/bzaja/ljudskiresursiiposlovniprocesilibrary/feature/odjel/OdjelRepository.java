package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OdjelRepository extends PagingAndSortingRepository<Odjel, Integer>, JpaSpecificationExecutor<Odjel> {

}
