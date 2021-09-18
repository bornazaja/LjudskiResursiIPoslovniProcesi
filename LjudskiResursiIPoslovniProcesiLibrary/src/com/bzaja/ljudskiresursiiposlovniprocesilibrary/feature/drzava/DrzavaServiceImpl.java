package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava;

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
public class DrzavaServiceImpl implements DrzavaService {

    @Autowired
    private DrzavaRepository drzavaRepository;

    @Override
    public void save(DrzavaDto drzavaDto) {
        Drzava drzava = ObjectMapperUtils.map(drzavaDto, Drzava.class);
        drzavaRepository.save(drzava);
    }

    @Override
    public void delete(Integer id) {
        drzavaRepository.deleteById(id);
    }

    @Override
    public DrzavaDto findById(Integer id) {
        Drzava drzava = drzavaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(drzava, DrzavaDto.class);
    }

    @Override
    public List<DrzavaDto> findAll() {
        List<Drzava> drzave = (List<Drzava>) drzavaRepository.findAll();
        return ObjectMapperUtils.mapAll(drzave, DrzavaDto.class);
    }

    @Override
    public Page<DrzavaDto> findAll(Pageable pageable) {
        Page<Drzava> drzavaPage = drzavaRepository.findAll(pageable);
        List<DrzavaDto> drzaveDto = ObjectMapperUtils.mapAll(drzavaPage.getContent(), DrzavaDto.class);
        return new PageImpl(drzaveDto, pageable, drzavaPage.getTotalElements());
    }

    @Override
    public Page<DrzavaDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<Drzava> drzavaPage = drzavaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<DrzavaDto> drzaveDto = ObjectMapperUtils.mapAll(drzavaPage.getContent(), DrzavaDto.class);
        return new PageImpl(drzaveDto, queryCriteriaDto.getPageable(), drzavaPage.getTotalElements());
    }

    @Override
    public List<DrzavaDto> findAll(String text, String... columns) {
        List<Drzava> drzave = drzavaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(drzave, DrzavaDto.class);
    }
}
