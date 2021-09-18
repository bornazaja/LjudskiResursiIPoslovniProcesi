package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransakcijaService {

    void save(TransakcijaDto transakcijaDto);

    void delete(Integer id);

    TransakcijaDto findById(Integer id);

    List<TransakcijaDto> findAll();

    TransakcijaResultListDto getTransakcijaResultList(LocalDate datumOd, LocalDate datumDo);

    Double getTrenutniProfit();

    Long count();

    List<TransakcijaStatisticsDto> findBrojTransakcijaPoVrstiUTrenutnojGodini();

    Page<TransakcijaDto> findAll(Pageable pageable);

    Page<TransakcijaDto> findAll(QueryCriteriaDto queryCriteriaDto);
}
