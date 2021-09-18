package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OdjelServiceImpl implements OdjelService {

    @Autowired
    private OdjelRepository odjelRepository;

    @Override
    public void save(OdjelDto odjelDto) {
        Odjel odjel = ObjectMapperUtils.map(odjelDto, Odjel.class);
        odjelRepository.save(odjel);
    }

    @Override
    public void delete(Integer id) {
        odjelRepository.deleteById(id);
    }

    @Override
    public OdjelDto findById(Integer id) {
        Odjel odjel = odjelRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(odjel, OdjelDto.class);
    }

    @Override
    public List<OdjelDto> findAll() {
        List<Odjel> odjeli = (List<Odjel>) odjelRepository.findAll();
        return ObjectMapperUtils.mapAll(odjeli, OdjelDto.class);
    }

    @Override
    public Long count() {
        return odjelRepository.count();
    }

    @Override
    public Page<OdjelDto> findAll(Pageable pageable) {
        Page<Odjel> odjelPage = odjelRepository.findAll(pageable);
        List<OdjelDto> odjeli = ObjectMapperUtils.mapAll(odjelPage.getContent(), OdjelDto.class);
        return new PageImpl(odjeli, pageable, odjelPage.getTotalElements());
    }

    @Override
    public Page<OdjelDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<Odjel> odjelPage = odjelRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<OdjelDto> odjeli = ObjectMapperUtils.mapAll(odjelPage.getContent(), OdjelDto.class);
        return new PageImpl(odjeli, queryCriteriaDto.getPageable(), odjelPage.getTotalElements());
    }

    @Override
    public List<OdjelDto> finaAll(String text, String... columns) {
        List<Odjel> odjeli = odjelRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(odjeli, OdjelDto.class);
    }
}
