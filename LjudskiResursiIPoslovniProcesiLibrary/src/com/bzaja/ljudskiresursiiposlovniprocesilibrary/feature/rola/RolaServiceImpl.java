package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RolaServiceImpl implements RolaService {

    @Autowired
    private RolaRepository rolaRepository;

    @Override
    public RolaDto findById(Integer id) {
        Rola rola = rolaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(rola, RolaDto.class);
    }

    @Override
    public List<RolaDto> findAll() {
        List<Rola> role = (List<Rola>) rolaRepository.findAll();
        return ObjectMapperUtils.mapAll(role, RolaDto.class);
    }

    @Override
    public List<RolaDto> findAll(String text, String... columns) {
        List<Rola> role = rolaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(role, RolaDto.class);
    }
}
