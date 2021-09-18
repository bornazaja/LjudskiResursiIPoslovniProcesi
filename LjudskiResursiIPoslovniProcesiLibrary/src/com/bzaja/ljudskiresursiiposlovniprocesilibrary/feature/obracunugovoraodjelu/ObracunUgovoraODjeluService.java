package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ObracunUgovoraODjeluService {

    void save(AddObracunUgovoraODjeluDto addObracunUgovoraODjeluDto);

    void delete(Integer id);

    ObracunUgovoraODjeluDto findById(Integer id);

    List<ObracunUgovoraODjeluDto> findAll();

    Page<ObracunUgovoraODjeluDto> findAll(Pageable pageable);

    Page<ObracunUgovoraODjeluDto> findAll(QueryCriteriaDto queryCriteriaDto);
}
