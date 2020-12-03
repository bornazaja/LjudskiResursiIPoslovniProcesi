package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ObracunUgovoraORaduRepository extends PagingAndSortingRepository<ObracunUgovoraORadu, Integer>, JpaSpecificationExecutor<ObracunUgovoraORadu> {

}
