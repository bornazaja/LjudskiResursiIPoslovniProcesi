package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IsplatnaListaService {

    IsplatnaListaDto findById(Integer id);

    List<IsplatnaListaDto> findAllByIdObracunUgovora(Integer idObracunUgovora);

    Page<IsplatnaListaDto> findAllByIdObracunUgovora(Integer idObracunUgovora, Pageable pageable);

    Page<IsplatnaListaDto> findAllByIdObracunUgovora(Integer idObracunUgovora, QueryCriteriaDto queryCriteriaDto);

    Page<IsplatnaListaDto> findAllByIdZaposlenik(Integer idZaposlenik, Pageable pageable);

    Page<IsplatnaListaDto> findAllByIdZaposlenik(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto);

    List<IsplatnaListaDto> findAllByIdZaposlenik(Integer idZaposlenik);
}
