package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ZaposlenikRolaService {

    void save(ZaposlenikRolaDto zaposlenikRolaDto);

    void delete(Integer id);

    ZaposlenikRolaDto findById(Integer id);

    List<ZaposlenikRolaDto> findAll();

    Page<ZaposlenikRolaDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<ZaposlenikRolaDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<ZaposlenikRolaDto> findAll(Integer idZaposlenik);
}
