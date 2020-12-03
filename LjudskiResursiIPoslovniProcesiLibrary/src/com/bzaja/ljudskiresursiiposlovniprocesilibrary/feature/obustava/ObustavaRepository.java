package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ObustavaRepository extends PagingAndSortingRepository<Obustava, Integer>, JpaSpecificationExecutor<Obustava> {

    @Query("select o from Obustava o where not(o.datumOd > :datumDo or o.datumDo < :datumOd or o.zaposlenik.idZaposlenik != :idZaposlenik) or (o.datumOd <= :datumDo and o.datumDo is null and o.zaposlenik.idZaposlenik = :idZaposlenik)")
    List<Obustava> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(@Param("datumOd") LocalDate datumOd, @Param("datumDo") LocalDate datumDo, @Param("idZaposlenik") Integer idZaposlenik);

    @Query("select count(o) from Obustava o where not(o.datumOd > current_date() or o.datumDo < current_date() or o.zaposlenik.idZaposlenik != :idZaposlenik) or (o.datumOd <= current_date() and o.datumDo is null and o.zaposlenik.idZaposlenik = :idZaposlenik)")
    Long countAktivneObustaveByZaposlenikId(@Param("idZaposlenik") Integer idZaposlenik);

}
