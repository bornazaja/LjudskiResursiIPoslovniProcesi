package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrebivalisteService {

    void save(PrebivalisteDto prebivalisteDto);

    void delete(Integer id);

    PrebivalisteDto findById(Integer id);

    List<PrebivalisteDto> findAll();

    Page<PrebivalisteDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<PrebivalisteDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<PrebivalisteDto> findAll(Integer idZaposlenik);

    PrebivalisteDto findFirstByZaposlenikIdZaposlenikOrderByDatumOdDesc(Integer idZaposlenik);
}
