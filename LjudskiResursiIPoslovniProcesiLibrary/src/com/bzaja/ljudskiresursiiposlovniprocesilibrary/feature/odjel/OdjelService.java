package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OdjelService {

    void save(OdjelDto odjelDto);

    void delete(Integer id);

    OdjelDto findById(Integer id);

    List<OdjelDto> findAll();

    Page<OdjelDto> findAll(Pageable pageable);

    Page<OdjelDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<OdjelDto> finaAll(String text, String... columns);

    Long count();
}
