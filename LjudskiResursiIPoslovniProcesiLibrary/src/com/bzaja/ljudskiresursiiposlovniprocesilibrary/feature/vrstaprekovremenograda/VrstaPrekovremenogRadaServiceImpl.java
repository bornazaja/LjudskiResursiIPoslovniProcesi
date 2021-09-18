package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda;

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
public class VrstaPrekovremenogRadaServiceImpl implements VrstaPrekovremenogRadaService {

    @Autowired
    private VrstaPrekovremenogRadaRepository vrstaPrekovremenogRadaRepository;

    @Override
    public void save(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        VrstaPrekovremenogRada vrstaPrekovremenogRada = ObjectMapperUtils.map(vrstaPrekovremenogRadaDto, VrstaPrekovremenogRada.class);
        vrstaPrekovremenogRadaRepository.save(vrstaPrekovremenogRada);
    }

    @Override
    public void delete(Integer id) {
        vrstaPrekovremenogRadaRepository.deleteById(id);
    }

    @Override
    public VrstaPrekovremenogRadaDto findById(Integer id) {
        VrstaPrekovremenogRada vrstaPrekovremenogRada = vrstaPrekovremenogRadaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaPrekovremenogRada, VrstaPrekovremenogRadaDto.class);
    }

    @Override
    public List<VrstaPrekovremenogRadaDto> findAll() {
        List<VrstaPrekovremenogRada> vrstePrekovremenogRada = (List<VrstaPrekovremenogRada>) vrstaPrekovremenogRadaRepository.findAll();
        return ObjectMapperUtils.mapAll(vrstePrekovremenogRada, VrstaPrekovremenogRadaDto.class);
    }

    @Override
    public Page<VrstaPrekovremenogRadaDto> findAll(Pageable pageable) {
        Page<VrstaPrekovremenogRada> vrstaPrekovremenogRadaPage = vrstaPrekovremenogRadaRepository.findAll(pageable);
        List<VrstaPrekovremenogRadaDto> vrstaPrekovremenihRadova = ObjectMapperUtils.mapAll(vrstaPrekovremenogRadaPage.getContent(), VrstaPrekovremenogRadaDto.class);
        return new PageImpl(vrstaPrekovremenihRadova, pageable, vrstaPrekovremenogRadaPage.getTotalElements());
    }

    @Override
    public Page<VrstaPrekovremenogRadaDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<VrstaPrekovremenogRada> vrstaPrekovremenogRadaPage = vrstaPrekovremenogRadaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<VrstaPrekovremenogRadaDto> vrstaPrekovremenihRadova = ObjectMapperUtils.mapAll(vrstaPrekovremenogRadaPage.getContent(), VrstaPrekovremenogRadaDto.class);
        return new PageImpl(vrstaPrekovremenihRadova, queryCriteriaDto.getPageable(), vrstaPrekovremenogRadaPage.getTotalElements());
    }

    @Override
    public List<VrstaPrekovremenogRadaDto> findAll(String text, String... columns) {
        List<VrstaPrekovremenogRada> vrstePrekovremenihRadova = vrstaPrekovremenogRadaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrstePrekovremenihRadova, VrstaPrekovremenogRadaDto.class);
    }
}
