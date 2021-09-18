package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GradService {

    void save(GradDto gradDto);

    void delete(Integer id);

    GradDto findById(Integer id);

    List<GradDto> findAll();

    Page<GradDto> findAll(Pageable pageable);

    Page<GradDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<GradDto> findAll(String text, String... columns);

    List<GradDto> findAllByDrzavaJeDomovinaTrue(String text, String... columns);
}
