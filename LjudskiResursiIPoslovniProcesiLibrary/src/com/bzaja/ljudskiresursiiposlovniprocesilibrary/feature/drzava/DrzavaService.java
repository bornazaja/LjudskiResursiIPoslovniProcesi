package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DrzavaService {

    void save(DrzavaDto drzavaDto);

    void delete(Integer id);

    DrzavaDto findById(Integer id);

    List<DrzavaDto> findAll();

    Page<DrzavaDto> findAll(Pageable pageable);

    Page<DrzavaDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<DrzavaDto> findAll(String text, String... columns);
}
