package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransakcijaRepository extends PagingAndSortingRepository<Transakcija, Integer>, JpaSpecificationExecutor<Transakcija> {

    List<Transakcija> findAllByDatumTransakcijeBetween(LocalDate datumOd, LocalDate datumDo);

    @Query("select new com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaStatisticsDto(t.vrstaTransakcije.naziv, count(t)) from Transakcija t where year(t.datumTransakcije) = year(current_date()) group by t.vrstaTransakcije.naziv")
    List<TransakcijaStatisticsDto> findBrojTransakcjaPoVrstiUTrenutnojGodini();
}
