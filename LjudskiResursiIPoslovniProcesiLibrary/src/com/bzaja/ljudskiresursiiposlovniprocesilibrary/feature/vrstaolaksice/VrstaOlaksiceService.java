package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VrstaOlaksiceService {

    void save(VrstaOlaksiceDto vrstaOlaksiceDto);

    void delete(Integer id);

    VrstaOlaksiceDto findById(Integer id);

    List<VrstaOlaksiceDto> findAll();

    Page<VrstaOlaksiceDto> findAll(Pageable pageable);

    Page<VrstaOlaksiceDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<VrstaOlaksiceDto> findAll(String text, String... columns);
}
