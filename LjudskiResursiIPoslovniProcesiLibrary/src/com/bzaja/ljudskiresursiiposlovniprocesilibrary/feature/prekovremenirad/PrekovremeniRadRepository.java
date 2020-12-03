package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PrekovremeniRadRepository extends PagingAndSortingRepository<PrekovremeniRad, Integer>, JpaSpecificationExecutor<PrekovremeniRad> {

    @Query("select pr from PrekovremeniRad pr where not(pr.datumOd > :datumDo or pr.datumDo < :datumOd or pr.zaposlenik.idZaposlenik != :idZaposlenik) or (pr.datumOd <= :datumDo and pr.datumOd is null and pr.zaposlenik.idZaposlenik = :idZaposlenik)")
    List<PrekovremeniRad> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(@Param("datumOd") LocalDate datumOd, @Param("datumDo") LocalDate datumDo, @Param("idZaposlenik") Integer idZaposlenik);

    @Query("select count(pr) from PrekovremeniRad pr where not(pr.datumOd > current_date() or pr.datumDo < current_date() or pr.zaposlenik.idZaposlenik != :idZaposlenik) or (pr.datumOd <= current_date() and pr.datumDo is null and pr.zaposlenik.idZaposlenik = :idZaposlenik)")
    Long countAktivniPrekovremeniRadoviByZaposlenikId(@Param("idZaposlenik") Integer idZaposlenik);
}
