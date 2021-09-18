package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UgovorODjeluService {

    void save(UgovorODjeluDto ugovorODjeluDto);

    void delete(Integer id);

    UgovorODjeluDto findById(Integer id);

    List<UgovorODjeluDto> findAll();

    List<UgovorODjeluDto> findByIdUgovorIn(List<Integer> ids);

    Page<UgovorODjeluDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<UgovorODjeluDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<UgovorODjeluDto> findAll(Integer idZaposlenik);

    List<UgovorODjeluDto> findAllNeObraucunateUgovore(String text, String... columns);
}
