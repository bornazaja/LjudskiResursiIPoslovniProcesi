package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol;

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
public class SpolServiceImpl implements SpolService {

    @Autowired
    private SpolRepository spolRepository;

    @Override
    public SpolDto findById(Integer id) {
        Spol spol = spolRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(spol, SpolDto.class);
    }

    @Override
    public List<SpolDto> findAll() {
        List<Spol> spolovi = (List<Spol>) spolRepository.findAll();
        return ObjectMapperUtils.mapAll(spolovi, SpolDto.class);
    }

    @Override
    public List<SpolDto> findAll(String text, String... columns) {
        List<Spol> spolovi = spolRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(spolovi, SpolDto.class);
    }
}
