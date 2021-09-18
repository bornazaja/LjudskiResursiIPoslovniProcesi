package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrekovremeniRadService {

    void save(PrekovremeniRadDto prekovremeniRadDto);

    void delete(Integer id);

    PrekovremeniRadDto findById(Integer id);

    List<PrekovremeniRadDto> findAll();

    List<PrekovremeniRadDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik);

    Page<PrekovremeniRadDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<PrekovremeniRadDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<PrekovremeniRadDto> findAll(Integer idZaposlenik);

    Long countAktivniPrekovremeniRadoviByZaposlenikId(Integer idZaposlenik);
}
