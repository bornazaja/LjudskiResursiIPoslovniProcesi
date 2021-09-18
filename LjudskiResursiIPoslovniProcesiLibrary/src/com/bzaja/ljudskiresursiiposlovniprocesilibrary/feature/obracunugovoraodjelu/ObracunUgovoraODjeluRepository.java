package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ObracunUgovoraODjeluRepository extends PagingAndSortingRepository<ObracunUgovoraODjelu, Integer>, JpaSpecificationExecutor<ObracunUgovoraODjelu> {

}
