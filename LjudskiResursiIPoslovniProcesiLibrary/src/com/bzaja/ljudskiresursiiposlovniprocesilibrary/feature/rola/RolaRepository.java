package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RolaRepository extends PagingAndSortingRepository<Rola, Integer>, JpaSpecificationExecutor<Rola> {

}
