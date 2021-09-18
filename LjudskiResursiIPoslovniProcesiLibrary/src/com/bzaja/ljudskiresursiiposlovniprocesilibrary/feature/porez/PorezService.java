package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PorezService {

    void save(PorezDto porezDto);

    void delete(Integer id);

    PorezDto findById(Integer id);

    List<PorezDto> findAll();

    Page<PorezDto> findAll(Pageable pageable);

    Page<PorezDto> findAll(QueryCriteriaDto queryCriteriaDto);

    Double findStopaByOsnovicaAndVrstaPorezaId(Double osnovica, Integer idVrstaPoreza);

    List<PorezDto> findAllByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrue(Integer idVrstaPoreza);

    PorezDto findFirstByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrueOrderByStopaAsc(Integer idVrstaPoreza);
}
