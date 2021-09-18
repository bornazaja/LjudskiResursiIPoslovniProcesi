package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VrstaPrekovremenogRadaRepository extends PagingAndSortingRepository<VrstaPrekovremenogRada, Integer>, JpaSpecificationExecutor<VrstaPrekovremenogRada> {

}
