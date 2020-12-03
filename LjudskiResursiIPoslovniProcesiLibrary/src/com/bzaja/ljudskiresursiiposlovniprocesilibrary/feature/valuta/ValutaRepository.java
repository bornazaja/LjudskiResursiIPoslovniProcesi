package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ValutaRepository extends PagingAndSortingRepository<Valuta, Integer>, JpaSpecificationExecutor<Valuta> {

    Integer countByDatumTecaja(LocalDate datumTecaja);

    Valuta findByNaziv(String naziv);

    Valuta findByDrzaveJeDomovinaTrue();
}
