package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentskiUgovorRepository extends PagingAndSortingRepository<StudentskiUgovor, Integer>, JpaSpecificationExecutor<StudentskiUgovor> {

    List<StudentskiUgovor> findByIdUgovorIn(List<Integer> ids);

    List<StudentskiUgovor> findByIsplatneListeObracunUgovoraIdObracunUgovora(Integer idObracunUgovora);
}
