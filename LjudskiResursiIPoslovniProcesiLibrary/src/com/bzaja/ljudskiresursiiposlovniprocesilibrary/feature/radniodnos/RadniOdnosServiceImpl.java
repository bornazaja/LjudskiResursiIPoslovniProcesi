package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RadniOdnosServiceImpl implements RadniOdnosService {

    @Autowired
    private RadniOdnosRepository radniOdnosRepository;

    @Override
    public RadniOdnosDto findById(Integer id) {
        RadniOdnos radniOdnos = radniOdnosRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(radniOdnos, RadniOdnosDto.class);
    }

    @Override
    public List<RadniOdnosDto> findAll() {
        List<RadniOdnos> radniOdnosi = (List<RadniOdnos>) radniOdnosRepository.findAll();
        return ObjectMapperUtils.mapAll(radniOdnosi, RadniOdnosDto.class);
    }

    @Override
    public List<RadniOdnosDto> findAll(String text, Integer idVrstaUgovora, String... columns) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idVrstaUgovora, SearchOperation.EQUAL, "vrstaUgovora.idVrstaUgovora"), Operator.AND))
                .and(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));

        List<RadniOdnos> radniOdnosi = radniOdnosRepository.findAll(specification);
        return ObjectMapperUtils.mapAll(radniOdnosi, RadniOdnosDto.class);
    }
}
