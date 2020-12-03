package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface DavanjeRepository extends PagingAndSortingRepository<Davanje, Integer>, JpaSpecificationExecutor<Davanje> {

    @Query("select d from Davanje d where not(d.datumOd > :datumDo or d.datumDo < :datumOd or d.zaposlenik.idZaposlenik != :idZaposlenik) "
            + "or (d.datumOd <= :datumDo and d.datumDo is null and d.zaposlenik.idZaposlenik = :idZaposlenik) "
            + "and (d.vrstaDavanja.vrijediDo >= :datumDo or d.vrstaDavanja.vrijediDo is null)")
    List<Davanje> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(@Param("datumOd") LocalDate datumOd, @Param("datumDo") LocalDate datumDo, @Param("idZaposlenik") Integer idZaposlenik);

    @Query("select count(d) from Davanje d where not(d.datumOd > current_date() or d.datumDo < current_date() or d.zaposlenik.idZaposlenik != :idZaposlenik) "
            + "or (d.datumOd <= current_date() and d.datumDo is null and d.zaposlenik.idZaposlenik = :idZaposlenik)")
    Long countAktivnaDavanjaByZaposlenikId(@Param("idZaposlenik") Integer idZaposlenik);
}
