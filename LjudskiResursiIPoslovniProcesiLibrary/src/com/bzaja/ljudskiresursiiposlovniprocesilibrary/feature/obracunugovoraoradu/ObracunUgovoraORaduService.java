package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ObracunUgovoraORaduService {

    void save(ObracunUgovoraORaduDto obracunUgovoraORaduDto);

    void delete(Integer id);

    ObracunUgovoraORaduDto findById(Integer id);

    List<ObracunUgovoraORaduDto> findAll();

    Page<ObracunUgovoraORaduDto> findAll(Pageable pageable);

    Page<ObracunUgovoraORaduDto> findAll(QueryCriteriaDto queryCriteriaDto);
}
