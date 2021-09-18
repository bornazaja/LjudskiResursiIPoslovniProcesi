package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PoslovniPartnerService {

    void save(PoslovniPartnerDto poslovniPartnerDto);

    void delete(Integer id);

    PoslovniPartnerDto findById(Integer id);

    List<PoslovniPartnerDto> findAll();

    Boolean existsByOibAndIdPoslovniPartnerNot(String oib, Integer idPoslovniPartner);

    Boolean existsByEmailAndIdPoslovniPartnerNot(String email, Integer idPoslovniPartner);

    Boolean existsByOib(String oib);

    Boolean existsByEmail(String email);

    Long count();

    Page<PoslovniPartnerDto> findAll(Pageable pageable);

    Page<PoslovniPartnerDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<PoslovniPartnerDto> findAll(String text, String... columns);
}
