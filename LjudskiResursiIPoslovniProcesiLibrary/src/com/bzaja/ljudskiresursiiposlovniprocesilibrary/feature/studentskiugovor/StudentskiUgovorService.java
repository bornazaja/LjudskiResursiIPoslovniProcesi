package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentskiUgovorService {

    void save(StudentskiUgovorDto studentskiUgovorDto);

    void delete(Integer id);

    StudentskiUgovorDto findById(Integer id);

    List<StudentskiUgovorDto> findAll();

    List<StudentskiUgovorDto> findByIdUgovorIn(List<Integer> ids);

    Page<StudentskiUgovorDto> findAll(Integer idZaposlenik, Pageable pageable);

    Page<StudentskiUgovorDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<StudentskiUgovorDto> findAll(Integer idZaposlenik);

    List<StudentskiUgovorDto> findAllNeObracunateUgovore(String text, String... columns);
}
