package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VrstaDavanjaService {

    void save(VrstaDavanjaDto vrstaDavanjaDto);

    void delete(Integer id);

    VrstaDavanjaDto findById(Integer id);

    List<VrstaDavanjaDto> findAll();

    Page<VrstaDavanjaDto> findAll(Pageable pageable);

    Page<VrstaDavanjaDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<VrstaDavanjaDto> findAll(String text, String... columns);
}
