package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface OlaksicaRepository extends PagingAndSortingRepository<Olaksica, Integer>, JpaSpecificationExecutor<Olaksica> {

    @Query("select o from Olaksica o where not(o.datumOd > :datumDo or o.datumDo < :datumOd or o.zaposlenik.idZaposlenik != :idZaposlenik) "
            + "or (o.datumOd <= :datumDo and o.datumDo is null and o.zaposlenik.idZaposlenik = :idZaposlenik) "
            + "and (o.vrstaOlaksice.vrijediDo >= :datumDo or o.vrstaOlaksice.vrijediDo is null)")
    List<Olaksica> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(@Param("datumOd") LocalDate datumOd, @Param("datumDo") LocalDate datumDo, @Param("idZaposlenik") Integer idZaposlenik);

    @Query("select count(o) from Olaksica o where not(o.datumOd > current_date() or o.datumDo < current_date() or o.zaposlenik.idZaposlenik != :idZaposlenik) "
            + "or (o.datumOd <= current_date() and o.datumDo is null and o.zaposlenik.idZaposlenik = :idZaposlenik)")
    Long countAktivneOlaksiceByZaposlenikId(@Param("idZaposlenik") Integer idZaposlenik);
}
