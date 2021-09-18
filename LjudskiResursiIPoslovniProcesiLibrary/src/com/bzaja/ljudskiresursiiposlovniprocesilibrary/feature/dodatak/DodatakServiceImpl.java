package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DodatakServiceImpl implements DodatakService {

    @Autowired
    private DodatakRepository dodatakRepository;

    @Override
    public void save(DodatakDto dodatakDto) {
        Dodatak dodatak = ObjectMapperUtils.map(dodatakDto, Dodatak.class);
        dodatakRepository.save(dodatak);
    }

    @Override
    public void delete(Integer id) {
        dodatakRepository.deleteById(id);
    }

    @Override
    public DodatakDto findById(Integer id) {
        Dodatak dodatak = dodatakRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(dodatak, DodatakDto.class);
    }

    @Override
    public List<DodatakDto> findAll() {
        List<Dodatak> dodatci = (List<Dodatak>) dodatakRepository.findAll();
        return ObjectMapperUtils.mapAll(dodatci, DodatakDto.class);
    }

    @Override
    public Page<DodatakDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<Dodatak> dodatakPage = dodatakRepository.findAll(specification, pageable);
        List<DodatakDto> dodatci = ObjectMapperUtils.mapAll(dodatakPage.getContent(), DodatakDto.class);
        return new PageImpl(dodatci, pageable, dodatakPage.getTotalElements());
    }

    @Override
    public Page<DodatakDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<Dodatak> dodatakPage = dodatakRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<DodatakDto> dodatci = ObjectMapperUtils.mapAll(dodatakPage.getContent(), DodatakDto.class);
        return new PageImpl(dodatci, queryCriteriaDto.getPageable(), dodatakPage.getTotalElements());
    }

    @Override
    public List<DodatakDto> findAll(Integer idZaposlenik) {
        List<Dodatak> dodatci = dodatakRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(dodatci, DodatakDto.class);
    }

    @Override
    public List<DodatakDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<Dodatak> dodatci = (List<Dodatak>) dodatakRepository.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        return ObjectMapperUtils.mapAll(dodatci, DodatakDto.class);
    }

    @Override
    public Long countAkivneDodatkeByZaposlenikId(Integer idZaposlenik) {
        return dodatakRepository.countAktivneDodatkeByZaposlenikId(idZaposlenik);
    }
}
