package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DodatakService {

    void save(DodatakDto dodatakDto);

    void delete(Integer id);

    DodatakDto findById(Integer id);

    List<DodatakDto> findAll();

    Page<DodatakDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<DodatakDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<DodatakDto> findAll(Integer idZaposlenik);

    List<DodatakDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik);

    Long countAkivneDodatkeByZaposlenikId(Integer idZaposlenik);
}
