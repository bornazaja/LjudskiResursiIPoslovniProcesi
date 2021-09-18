package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice;

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
public class VrstaOlaksiceServiceImpl implements VrstaOlaksiceService {

    @Autowired
    private VrstaOlaksiceRepository vrstaOlaksiceRepository;

    @Override
    public void save(VrstaOlaksiceDto vrstaOlaksiceDto) {
        VrstaOlaksice vrstaOlaksice = ObjectMapperUtils.map(vrstaOlaksiceDto, VrstaOlaksice.class);
        vrstaOlaksiceRepository.save(vrstaOlaksice);
    }

    @Override
    public void delete(Integer id) {
        vrstaOlaksiceRepository.deleteById(id);
    }

    @Override
    public VrstaOlaksiceDto findById(Integer id) {
        VrstaOlaksice vrstaOlaksice = vrstaOlaksiceRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaOlaksice, VrstaOlaksiceDto.class);
    }

    @Override
    public List<VrstaOlaksiceDto> findAll() {
        List<VrstaOlaksice> vrsteolaksica = (List<VrstaOlaksice>) vrstaOlaksiceRepository.findAll();
        return ObjectMapperUtils.mapAll(vrsteolaksica, VrstaOlaksiceDto.class);
    }

    @Override
    public Page<VrstaOlaksiceDto> findAll(Pageable pageable) {
        Page<VrstaOlaksice> vrstaOlaksicePage = vrstaOlaksiceRepository.findAll(pageable);
        List<VrstaOlaksiceDto> vrsteOlaksica = ObjectMapperUtils.mapAll(vrstaOlaksicePage.getContent(), VrstaOlaksiceDto.class);
        return new PageImpl(vrsteOlaksica, pageable, vrstaOlaksicePage.getTotalElements());
    }

    @Override
    public Page<VrstaOlaksiceDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<VrstaOlaksice> vrstaOlaksicaPage = vrstaOlaksiceRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<VrstaOlaksiceDto> vrstaOlaksica = ObjectMapperUtils.mapAll(vrstaOlaksicaPage.getContent(), VrstaOlaksiceDto.class);
        return new PageImpl(vrstaOlaksica, queryCriteriaDto.getPageable(), vrstaOlaksicaPage.getTotalElements());
    }

    @Override
    public List<VrstaOlaksiceDto> findAll(String text, String... columns) {
        List<VrstaOlaksice> vrsteOlaksica = vrstaOlaksiceRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrsteOlaksica, VrstaOlaksiceDto.class);
    }
}
