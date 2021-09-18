package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UgovorRepository extends CrudRepository<Ugovor, Integer> {

    @Query("select count(u) from Ugovor u where not(u.datumOd > current_date() or u.datumDo < current_date()) or (u.datumOd <= current_date() and u.datumDo is null)")
    Long countAktivneUgovore();

    Long countByZaposlenikIdZaposlenik(Integer idZaposlenik);

    @Query("select u from Ugovor u where not(u.datumOd > current_date() or u.datumDo < current_date() or u.zaposlenik.idZaposlenik != :idZaposlenik) or (u.datumOd <= current_date() and u.datumDo is null and u.zaposlenik.idZaposlenik = :idZaposlenik)")
    Ugovor findAktivniUgovorByZaposlenikId(@Param("idZaposlenik") Integer idZapolsenik);
}
