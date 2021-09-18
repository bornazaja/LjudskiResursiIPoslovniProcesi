package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ObustavaService {

    void save(ObustavaDto obustavaDto);

    void delete(Integer id);

    ObustavaDto findById(Integer id);

    List<ObustavaDto> findAll();

    Page<ObustavaDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<ObustavaDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<ObustavaDto> findAll(Integer idZaposlenik);

    List<ObustavaDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik);

    Long countAktivneObustaveByZaposlenikId(Integer idZaposlenik);
}
