package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DrzavaRepository extends PagingAndSortingRepository<Drzava, Integer>, JpaSpecificationExecutor<Drzava> {

}
