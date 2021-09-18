package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DavanjeService {

    void save(DavanjeDto davanjeDto);

    void delete(Integer id);

    DavanjeDto findById(Integer id);

    List<DavanjeDto> findAll();

    List<DavanjeDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik);

    Page<DavanjeDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<DavanjeDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<DavanjeDto> findAll(Integer idZaposlenik);

    Long countAktivnaDavanjaByZaposlenikId(Integer idZaposlenik);
}
