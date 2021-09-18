package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GradRepository extends PagingAndSortingRepository<Grad, Integer>, JpaSpecificationExecutor<Grad> {

}
