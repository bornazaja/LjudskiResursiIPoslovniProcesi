package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransakcijaServiceImpl implements TransakcijaService {

    @Autowired
    private TransakcijaRepository transakcijaRepository;

    @Autowired
    private TransakcijeKalkulator transakcijeKalkulator;

    @Override
    public void save(TransakcijaDto transakcijaDto) {
        Transakcija transakcija = ObjectMapperUtils.map(transakcijaDto, Transakcija.class);
        transakcijaRepository.save(transakcija);
    }

    @Override
    public void delete(Integer id) {
        transakcijaRepository.deleteById(id);
    }

    @Override
    public TransakcijaDto findById(Integer id) {
        Transakcija transakcija = transakcijaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(transakcija, TransakcijaDto.class);
    }

    @Override
    public List<TransakcijaDto> findAll() {
        List<Transakcija> transakcije = (List<Transakcija>) transakcijaRepository.findAll();
        return ObjectMapperUtils.mapAll(transakcije, TransakcijaDto.class);
    }

    @Override
    public TransakcijaResultListDto getTransakcijaResultList(LocalDate datumOd, LocalDate datumDo) {
        List<Transakcija> transakcije = (List<Transakcija>) transakcijaRepository.findAllByDatumTransakcijeBetween(datumOd, datumDo);
        List<TransakcijaDto> transakcijeDto = ObjectMapperUtils.mapAll(transakcije, TransakcijaDto.class);
        return transakcijeKalkulator.izracunaj(datumOd, datumDo, transakcijeDto);
    }

    @Override
    public Double getTrenutniProfit() {
        List<TransakcijaDto> transakcijeDto = ObjectMapperUtils.mapAll((List<Transakcija>) transakcijaRepository.findAll(), TransakcijaDto.class);
        return transakcijeKalkulator.izracunaj(LocalDate.now(), LocalDate.now(), transakcijeDto).getProfitNakonPorezaIPrireza();
    }

    @Override
    public Long count() {
        return transakcijaRepository.count();
    }

    @Override
    public List<TransakcijaStatisticsDto> findBrojTransakcijaPoVrstiUTrenutnojGodini() {
        return transakcijaRepository.findBrojTransakcjaPoVrstiUTrenutnojGodini();
    }

    @Override
    public Page<TransakcijaDto> findAll(Pageable pageable) {
        Page<Transakcija> transakcijaPage = transakcijaRepository.findAll(pageable);
        List<TransakcijaDto> transakcije = ObjectMapperUtils.mapAll(transakcijaPage.getContent(), TransakcijaDto.class);
        return new PageImpl(transakcije, pageable, transakcijaPage.getTotalElements());
    }

    @Override
    public Page<TransakcijaDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<Transakcija> transakcijaPage = transakcijaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<TransakcijaDto> transakcije = ObjectMapperUtils.mapAll(transakcijaPage.getContent(), TransakcijaDto.class);
        return new PageImpl(transakcije, queryCriteriaDto.getPageable(), transakcijaPage.getTotalElements());
    }
}
