package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OlaksicaService {

    void save(OlaksicaDto olaksicaDto);

    void delete(Integer id);

    OlaksicaDto findById(Integer id);

    List<OlaksicaDto> findAll();

    List<OlaksicaDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik);

    Page<OlaksicaDto> findAll(Integer idZaposelnik, Pageable pageable);

    Page<OlaksicaDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<OlaksicaDto> findAll(Integer idZaposlenik);

    Long countAktvneOlaksiceByZaposlenikId(Integer idZaposlenik);
}
