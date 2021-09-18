package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PrebivalisteRepository extends PagingAndSortingRepository<Prebivaliste, Integer>, JpaSpecificationExecutor<Prebivaliste> {

    Prebivaliste findFirstByZaposlenikIdZaposlenikOrderByDatumOdDesc(Integer idZaposlenik);
}
