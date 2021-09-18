package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface KategorijaTransakcijeRepository extends PagingAndSortingRepository<KategorijaTransakcije, Integer>, JpaSpecificationExecutor<KategorijaTransakcije> {

}
