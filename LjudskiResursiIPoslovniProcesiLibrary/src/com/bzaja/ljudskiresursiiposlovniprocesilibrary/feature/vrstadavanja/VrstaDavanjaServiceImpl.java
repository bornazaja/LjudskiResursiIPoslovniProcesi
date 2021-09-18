package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja;

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
public class VrstaDavanjaServiceImpl implements VrstaDavanjaService {

    @Autowired
    private VrstaDavanjaRepository vrstaDavanjaRepository;

    @Override
    public void save(VrstaDavanjaDto vrstaDavanjaDto) {
        VrstaDavanja vrstaDavanja = ObjectMapperUtils.map(vrstaDavanjaDto, VrstaDavanja.class);
        vrstaDavanjaRepository.save(vrstaDavanja);
    }

    @Override
    public void delete(Integer id) {
        vrstaDavanjaRepository.deleteById(id);
    }

    @Override
    public VrstaDavanjaDto findById(Integer id) {
        VrstaDavanja vrstaDavanja = vrstaDavanjaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaDavanja, VrstaDavanjaDto.class);
    }

    @Override
    public List<VrstaDavanjaDto> findAll() {
        List<VrstaDavanja> vrsteDavanja = (List<VrstaDavanja>) vrstaDavanjaRepository.findAll();
        return ObjectMapperUtils.mapAll(vrsteDavanja, VrstaDavanjaDto.class);
    }

    @Override
    public Page<VrstaDavanjaDto> findAll(Pageable pageable) {
        Page<VrstaDavanja> vrstaDavanjaPage = vrstaDavanjaRepository.findAll(pageable);
        List<VrstaDavanjaDto> vrsteDavanja = ObjectMapperUtils.mapAll(vrstaDavanjaPage.getContent(), VrstaDavanjaDto.class);
        return new PageImpl(vrsteDavanja, pageable, vrstaDavanjaPage.getTotalElements());
    }

    @Override
    public Page<VrstaDavanjaDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<VrstaDavanja> vrstaDavanjaPage = vrstaDavanjaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<VrstaDavanjaDto> vrsteDavanja = ObjectMapperUtils.mapAll(vrstaDavanjaPage.getContent(), VrstaDavanjaDto.class);
        return new PageImpl(vrsteDavanja, queryCriteriaDto.getPageable(), vrstaDavanjaPage.getTotalElements());
    }

    @Override
    public List<VrstaDavanjaDto> findAll(String text, String... columns) {
        List<VrstaDavanja> vrsteDavanja = vrstaDavanjaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrsteDavanja, VrstaDavanjaDto.class);
    }
}
