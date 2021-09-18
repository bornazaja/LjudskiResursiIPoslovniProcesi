package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VrstaDodatkaService {

    void save(VrstaDodatkaDto vrstaDodatkaDto);

    void delete(Integer id);

    VrstaDodatkaDto findById(Integer id);

    List<VrstaDodatkaDto> findAll();

    Page<VrstaDodatkaDto> findAll(Pageable pageable);

    Page<VrstaDodatkaDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<VrstaDodatkaDto> findAll(String text, String... columns);
}
