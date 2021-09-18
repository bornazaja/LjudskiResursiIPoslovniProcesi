package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KategorijaTransakcijeService {

    void save(KategorijaTransakcijeDto kategorijaTransakcijaDto);

    void delete(Integer id);

    KategorijaTransakcijeDto findById(Integer id);

    List<KategorijaTransakcijeDto> findAll();

    Page<KategorijaTransakcijeDto> findAll(Pageable pageable);

    Page<KategorijaTransakcijeDto> findAll(QueryCriteriaDto queryCriteriaDto);

    List<KategorijaTransakcijeDto> findAll(String text, String... columns);
}
