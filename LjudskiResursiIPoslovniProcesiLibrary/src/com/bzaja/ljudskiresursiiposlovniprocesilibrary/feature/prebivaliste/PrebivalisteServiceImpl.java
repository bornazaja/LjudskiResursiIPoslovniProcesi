package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
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
public class PrebivalisteServiceImpl implements PrebivalisteService {

    @Autowired
    private PrebivalisteRepository prebivalisteRepository;

    @Override
    public void save(PrebivalisteDto prebivalisteDto) {
        Prebivaliste prebivaliste = ObjectMapperUtils.map(prebivalisteDto, Prebivaliste.class);
        prebivalisteRepository.save(prebivaliste);
    }

    @Override
    public void delete(Integer id) {
        prebivalisteRepository.deleteById(id);
    }

    @Override
    public PrebivalisteDto findById(Integer id) {
        Prebivaliste prebivaliste = prebivalisteRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(prebivaliste, PrebivalisteDto.class);
    }

    @Override
    public List<PrebivalisteDto> findAll() {
        List<Prebivaliste> prebivalista = (List<Prebivaliste>) prebivalisteRepository.findAll();
        return ObjectMapperUtils.mapAll(prebivalista, PrebivalisteDto.class);
    }

    @Override
    public Page<PrebivalisteDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<Prebivaliste> prebivalistePage = prebivalisteRepository.findAll(specification, pageable);
        List<PrebivalisteDto> prebivalista = ObjectMapperUtils.mapAll(prebivalistePage.getContent(), PrebivalisteDto.class);
        return new PageImpl(prebivalista, pageable, prebivalistePage.getTotalElements());
    }

    @Override
    public Page<PrebivalisteDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<Prebivaliste> prebivalistePage = prebivalisteRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<PrebivalisteDto> prebivaliste = ObjectMapperUtils.mapAll(prebivalistePage.getContent(), PrebivalisteDto.class);
        return new PageImpl(prebivaliste, queryCriteriaDto.getPageable(), prebivalistePage.getTotalElements());
    }

    @Override
    public List<PrebivalisteDto> findAll(Integer idZaposlenik) {
        List<Prebivaliste> prebivalista = prebivalisteRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(prebivalista, PrebivalisteDto.class);
    }

    @Override
    public PrebivalisteDto findFirstByZaposlenikIdZaposlenikOrderByDatumOdDesc(Integer idZaposlenik) {
        Prebivaliste prebivaliste = prebivalisteRepository.findFirstByZaposlenikIdZaposlenikOrderByDatumOdDesc(idZaposlenik);
        return ObjectMapperUtils.map(prebivaliste, PrebivalisteDto.class);
    }
}
