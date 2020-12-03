package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SpolRepository extends PagingAndSortingRepository<Spol, Integer>, JpaSpecificationExecutor<Spol> {

}
