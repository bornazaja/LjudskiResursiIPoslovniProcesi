package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave;

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
public class VrstaObustaveServiceImpl implements VrstaObustaveService {

    @Autowired
    private VrstaObustaveRepository vrstaObustaveRepository;

    @Override
    public void save(VrstaObustaveDto vrstaObustaveDto) {
        VrstaObustave vrstaObustave = ObjectMapperUtils.map(vrstaObustaveDto, VrstaObustave.class);
        vrstaObustaveRepository.save(vrstaObustave);
    }

    @Override
    public void delete(Integer id) {
        vrstaObustaveRepository.deleteById(id);
    }

    @Override
    public VrstaObustaveDto findById(Integer id) {
        VrstaObustave vrstaObustave = vrstaObustaveRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaObustave, VrstaObustaveDto.class);
    }

    @Override
    public List<VrstaObustaveDto> findAll() {
        List<VrstaObustave> vrsteObustava = (List<VrstaObustave>) vrstaObustaveRepository.findAll();
        return ObjectMapperUtils.mapAll(vrsteObustava, VrstaObustaveDto.class);
    }

    @Override
    public Page<VrstaObustaveDto> findAll(Pageable pageable) {
        Page<VrstaObustave> vrstaObustavePage = vrstaObustaveRepository.findAll(pageable);
        List<VrstaObustaveDto> vrsteObustava = ObjectMapperUtils.mapAll(vrstaObustavePage.getContent(), VrstaObustaveDto.class);
        return new PageImpl(vrsteObustava, pageable, vrstaObustavePage.getTotalElements());
    }

    @Override
    public Page<VrstaObustaveDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<VrstaObustave> vrstaObustavaPage = vrstaObustaveRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<VrstaObustaveDto> vrsteObustava = ObjectMapperUtils.mapAll(vrstaObustavaPage.getContent(), VrstaObustaveDto.class);
        return new PageImpl(vrsteObustava, queryCriteriaDto.getPageable(), vrstaObustavaPage.getTotalElements());
    }

    @Override
    public List<VrstaObustaveDto> findAll(String text, String... columns) {
        List<VrstaObustave> vrsteObustava = vrstaObustaveRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrsteObustava, VrstaObustaveDto.class);
    }
}
