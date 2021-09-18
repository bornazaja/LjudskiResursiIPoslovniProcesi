package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UgovorORaduRepository extends PagingAndSortingRepository<UgovorORadu, Integer>, JpaSpecificationExecutor<UgovorORadu> {

    @Query("select uor from UgovorORadu uor where not(uor.datumOd > :datumDo or uor.datumDo < :datumOd) or (uor.datumOd <= :datumDo and uor.datumDo is null)")
    List<UgovorORadu> findAllInPeriodDatumOdAndDatumDo(@Param("datumOd") LocalDate datumOd, @Param("datumDo") LocalDate datumDo);
}
