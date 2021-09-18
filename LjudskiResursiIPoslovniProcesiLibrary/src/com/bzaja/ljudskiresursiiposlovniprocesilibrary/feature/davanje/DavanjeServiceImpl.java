package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje;

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
public class DavanjeServiceImpl implements DavanjeService {

    @Autowired
    private DavanjeRepository davanjeRepository;

    @Override
    public void save(DavanjeDto davanjeDto) {
        Davanje davanje = ObjectMapperUtils.map(davanjeDto, Davanje.class);
        davanjeRepository.save(davanje);
    }

    @Override
    public void delete(Integer id) {
        davanjeRepository.deleteById(id);
    }

    @Override
    public DavanjeDto findById(Integer id) {
        Davanje davanje = davanjeRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(davanje, DavanjeDto.class);
    }

    @Override
    public List<DavanjeDto> findAll() {
        List<Davanje> davanja = (List<Davanje>) davanjeRepository.findAll();
        return ObjectMapperUtils.mapAll(davanja, DavanjeDto.class);
    }

    @Override
    public List<DavanjeDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<Davanje> davanja = (List<Davanje>) davanjeRepository.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        return ObjectMapperUtils.mapAll(davanja, DavanjeDto.class);
    }

    @Override
    public Page<DavanjeDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<Davanje> davanjePage = davanjeRepository.findAll(specification, pageable);
        List<DavanjeDto> davanja = ObjectMapperUtils.mapAll(davanjePage.getContent(), DavanjeDto.class);
        return new PageImpl(davanja, pageable, davanjePage.getTotalElements());
    }

    @Override
    public Page<DavanjeDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<Davanje> davanjePage = davanjeRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<DavanjeDto> davanja = ObjectMapperUtils.mapAll(davanjePage.getContent(), DavanjeDto.class);
        return new PageImpl(davanja, queryCriteriaDto.getPageable(), davanjePage.getTotalElements());
    }

    @Override
    public List<DavanjeDto> findAll(Integer idZaposlenik) {
        List<Davanje> davanja = davanjeRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(davanja, DavanjeDto.class);
    }

    @Override
    public Long countAktivnaDavanjaByZaposlenikId(Integer idZaposlenik) {
        return davanjeRepository.countAktivnaDavanjaByZaposlenikId(idZaposlenik);
    }
}
