package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RadnoMjestoService {

    void save(RadnoMjestoDto radnoMjestoDto);

    void delete(Integer id);

    RadnoMjestoDto findById(Integer id);

    List<RadnoMjestoDto> findAll();

    Page<RadnoMjestoDto> findAll(Pageable pageable);

    Page<RadnoMjestoDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<RadnoMjestoDto> findAll(String text, String... columns);

    Long count();
}
