package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VrstaPrekovremenogRadaService {

    void save(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto);

    void delete(Integer id);

    VrstaPrekovremenogRadaDto findById(Integer id);

    List<VrstaPrekovremenogRadaDto> findAll();

    Page<VrstaPrekovremenogRadaDto> findAll(Pageable pageable);

    Page<VrstaPrekovremenogRadaDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<VrstaPrekovremenogRadaDto> findAll(String text, String... columns);
}
