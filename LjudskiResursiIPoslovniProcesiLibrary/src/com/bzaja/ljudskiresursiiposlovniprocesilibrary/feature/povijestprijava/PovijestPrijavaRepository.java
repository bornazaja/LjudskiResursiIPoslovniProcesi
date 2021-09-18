package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PovijestPrijavaRepository extends PagingAndSortingRepository<PovijestPrijava, Integer>, JpaSpecificationExecutor<PovijestPrijava> {

    long countByZaposlenikIdZaposlenik(Integer idZaposlenik);

    @Query("select pp from PovijestPrijava pp order by pp.vrijemePrijave desc")
    List<PovijestPrijava> findRecentPovijestPrijava(Pageable pageable);

    @Query("select new com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaStatisticsDto(cast(pp.vrijemePrijave as java.time.LocalDate), count(pp)) from PovijestPrijava pp "
            + "where year(pp.vrijemePrijave) = year(current_date()) "
            + "group by cast(pp.vrijemePrijave as java.time.LocalDate) "
            + "order by cast(pp.vrijemePrijave as java.time.LocalDate) desc")
    List<PovijestPrijavaStatisticsDto> findRecentDnevniBrojPrijava(Pageable pageable);
}
