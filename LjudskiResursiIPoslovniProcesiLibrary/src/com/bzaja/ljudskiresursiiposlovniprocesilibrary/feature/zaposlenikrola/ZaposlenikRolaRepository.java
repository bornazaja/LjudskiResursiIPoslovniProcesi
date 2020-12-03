package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ZaposlenikRolaRepository extends PagingAndSortingRepository<ZaposlenikRola, Integer>, JpaSpecificationExecutor<ZaposlenikRola> {

}
