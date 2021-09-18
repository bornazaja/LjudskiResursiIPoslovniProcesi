package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka;

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
public class VrstaDodatkaServiceImpl implements VrstaDodatkaService {

    @Autowired
    private VrstaDodatkaRepository vrstaDodatkaRepository;

    @Override
    public void save(VrstaDodatkaDto vrstaDodatkaDto) {
        VrstaDodatka vrstaDodatka = ObjectMapperUtils.map(vrstaDodatkaDto, VrstaDodatka.class);
        vrstaDodatkaRepository.save(vrstaDodatka);
    }

    @Override
    public void delete(Integer id) {
        vrstaDodatkaRepository.deleteById(id);
    }

    @Override
    public VrstaDodatkaDto findById(Integer id) {
        VrstaDodatka vrstaDodatka = vrstaDodatkaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaDodatka, VrstaDodatkaDto.class);
    }

    @Override
    public List<VrstaDodatkaDto> findAll() {
        List<VrstaDodatka> vrsteDodatka = (List<VrstaDodatka>) vrstaDodatkaRepository.findAll();
        return ObjectMapperUtils.mapAll(vrsteDodatka, VrstaDodatkaDto.class);
    }

    @Override
    public Page<VrstaDodatkaDto> findAll(Pageable pageable) {
        Page<VrstaDodatka> vrstaDodatkaPage = vrstaDodatkaRepository.findAll(pageable);
        List<VrstaDodatkaDto> vrsteDodataka = ObjectMapperUtils.mapAll(vrstaDodatkaPage.getContent(), VrstaDodatkaDto.class);
        return new PageImpl(vrsteDodataka, pageable, vrstaDodatkaPage.getTotalElements());
    }

    @Override
    public Page<VrstaDodatkaDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<VrstaDodatka> vrstaDodatkaPage = vrstaDodatkaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<VrstaDodatkaDto> vrsteDodataka = ObjectMapperUtils.mapAll(vrstaDodatkaPage.getContent(), VrstaDodatkaDto.class);
        return new PageImpl(vrsteDodataka, queryCriteriaDto.getPageable(), vrstaDodatkaPage.getTotalElements());
    }

    @Override
    public List<VrstaDodatkaDto> findAll(String text, String... columns) {
        List<VrstaDodatka> vrsteDodataka = vrstaDodatkaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrsteDodataka, VrstaDodatkaDto.class);
    }
}
