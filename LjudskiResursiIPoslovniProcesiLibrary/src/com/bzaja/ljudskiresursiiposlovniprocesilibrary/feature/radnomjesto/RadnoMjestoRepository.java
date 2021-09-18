package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RadnoMjestoRepository extends PagingAndSortingRepository<RadnoMjesto, Integer>, JpaSpecificationExecutor<RadnoMjesto> {

}
