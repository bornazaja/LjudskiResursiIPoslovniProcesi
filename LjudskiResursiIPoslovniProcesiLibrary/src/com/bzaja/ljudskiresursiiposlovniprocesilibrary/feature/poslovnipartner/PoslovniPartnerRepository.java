package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PoslovniPartnerRepository extends PagingAndSortingRepository<PoslovniPartner, Integer>, JpaSpecificationExecutor<PoslovniPartner> {

    Boolean existsByOibAndIdPoslovniPartnerNot(String oib, Integer idPoslovniPartner);

    Boolean existsByEmailAndIdPoslovniPartnerNot(String oib, Integer idPoslovniPartner);

    Boolean existsByOib(String oib);

    Boolean existsByEmail(String email);
}
