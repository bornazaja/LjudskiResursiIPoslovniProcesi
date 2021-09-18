package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ZaposlenikRepository extends PagingAndSortingRepository<Zaposlenik, Integer>, JpaSpecificationExecutor<Zaposlenik> {

    Zaposlenik findByEmailAndLozinkaAndZaposleniciRoleRolaIdRola(String email, String lozinka, Integer idRola);

    Boolean existsByOibAndIdZaposlenikNot(String oib, Integer idZaposlenik);

    Boolean existsByEmailAndIdZaposlenikNot(String email, Integer idZaposlenik);

    Boolean existsByOib(String oib);

    Boolean existsByEmail(String email);

    Boolean existsByIdZaposlenikAndLozinka(Integer idZaposlenik, String lozinka);
}
