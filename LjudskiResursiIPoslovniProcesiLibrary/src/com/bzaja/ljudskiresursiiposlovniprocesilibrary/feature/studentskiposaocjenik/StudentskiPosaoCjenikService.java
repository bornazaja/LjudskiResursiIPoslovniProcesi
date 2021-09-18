package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentskiPosaoCjenikService {

    void save(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto);

    void delete(Integer id);

    StudentskiPosaoCjenikDto findById(Integer id);

    List<StudentskiPosaoCjenikDto> findAll();

    Page<StudentskiPosaoCjenikDto> findAll(Pageable pageable);

    Page<StudentskiPosaoCjenikDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<StudentskiPosaoCjenikDto> findAll(String text, String... columns);
}
