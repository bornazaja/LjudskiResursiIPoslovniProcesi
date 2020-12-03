package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza;

import com.bzaja.myjavalibrary.springframework.data.query.EntitySearchSpecification;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.SearchCriteriaUtils;
import com.bzaja.myjavalibrary.springframework.data.query.SearchOperation;
import com.bzaja.myjavalibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VrstaPorezaServiceImpl implements VrstaPorezaService {

    @Autowired
    private VrstaPorezaRepository vrstaPorezaRepository;

    @Override
    public VrstaPorezaDto findById(Integer id) {
        VrstaPoreza vrstaPoreza = vrstaPorezaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaPoreza, VrstaPorezaDto.class);
    }

    @Override
    public List<VrstaPorezaDto> findAll() {
        List<VrstaPoreza> vrstePoreza = (List<VrstaPoreza>) vrstaPorezaRepository.findAll();
        return ObjectMapperUtils.mapAll(vrstePoreza, VrstaPorezaDto.class);
    }

    @Override
    public List<VrstaPorezaDto> findAll(String text, String... columns) {
        List<VrstaPoreza> vrstePoreza = vrstaPorezaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrstePoreza, VrstaPorezaDto.class);
    }
}
