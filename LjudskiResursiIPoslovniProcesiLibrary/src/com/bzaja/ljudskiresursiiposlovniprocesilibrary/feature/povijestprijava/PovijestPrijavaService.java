package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PovijestPrijavaService {

    PovijestPrijavaDto findById(Integer id);

    List<PovijestPrijavaDto> findAll();

    Long count();

    Long countByZaposlenikId(Integer idZaposlenik);

    List<PovijestPrijavaDto> findRecentPovijestPrijava(Integer n);

    List<PovijestPrijavaStatisticsDto> findRecentDnevniBrojPrijava(Integer n);

    Page<PovijestPrijavaDto> findAll(Pageable pageable);

    Page<PovijestPrijavaDto> findAll(QueryCriteriaDto queryCriteriaDto);
}
