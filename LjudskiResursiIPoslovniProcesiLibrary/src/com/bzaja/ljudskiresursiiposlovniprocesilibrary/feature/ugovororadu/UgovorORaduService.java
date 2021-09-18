package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UgovorORaduService {

    void save(UgovorORaduDto ugovorORaduDto);

    void delete(Integer id);

    UgovorORaduDto findById(Integer id);

    List<UgovorORaduDto> findAll();

    List<UgovorORaduDto> findAllInPeriodDatumOdAndDatumDo(LocalDate datumOd, LocalDate datumDo);

    Page<UgovorORaduDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<UgovorORaduDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<UgovorORaduDto> findAll(Integer idZaposlenik);
}
