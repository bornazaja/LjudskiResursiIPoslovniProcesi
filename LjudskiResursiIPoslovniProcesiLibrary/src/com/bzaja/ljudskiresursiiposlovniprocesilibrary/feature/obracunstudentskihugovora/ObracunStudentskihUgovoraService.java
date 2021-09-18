package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ObracunStudentskihUgovoraService {

    void save(AddObracunStudentskihUgovoraDto addObracunStudentskihUgovoraDto);

    void delete(Integer id);

    ObracunStudentskihUgovoraDto findById(Integer id);

    List<ObracunStudentskihUgovoraDto> findAll();

    Page<ObracunStudentskihUgovoraDto> findAll(Pageable pageable);

    Page<ObracunStudentskihUgovoraDto> findAll(QueryCriteriaDto queryCriteriaDto);
}
