package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PorezRepository extends PagingAndSortingRepository<Porez, Integer>, JpaSpecificationExecutor<Porez> {

    @Query("select p.stopa from Porez p where (:osnovica >= p.minOsnovica and :osnovica <= p.maxOsnovica and p.vrstaPoreza.idVrstaPoreza = :idVrstaPoreza) or (:osnovica > p.minOsnovica and p.maxOsnovica is null and p.vrstaPoreza.idVrstaPoreza = :idVrstaPoreza)")
    Double findStopaByOsnovicaAndVrstaPorezaId(@Param("osnovica") Double osnovica, @Param("idVrstaPoreza") Integer idVrstaPoreza);

    List<Porez> findAllByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrue(Integer idVrstaPoreza);

    Porez findFirstByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrueOrderByStopaAsc(Integer idVrstaPoreza);
}
