package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VrstaObustaveService {

    void save(VrstaObustaveDto vrstaObustaveDto);

    void delete(Integer id);

    VrstaObustaveDto findById(Integer id);

    List<VrstaObustaveDto> findAll();

    Page<VrstaObustaveDto> findAll(Pageable pageable);

    Page<VrstaObustaveDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<VrstaObustaveDto> findAll(String text, String... columns);
}
