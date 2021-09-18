package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GradServiceImpl implements GradService {

    @Autowired
    private GradRepository gradRepository;

    @Override
    public void save(GradDto gradDto) {
        Grad grad = ObjectMapperUtils.map(gradDto, Grad.class);
        gradRepository.save(grad);
    }

    @Override
    public void delete(Integer id) {
        gradRepository.deleteById(id);
    }

    @Override
    public GradDto findById(Integer id) {
        Grad grad = gradRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(grad, GradDto.class);
    }

    @Override
    public List<GradDto> findAll() {
        List<Grad> gradovi = (List<Grad>) gradRepository.findAll();
        return ObjectMapperUtils.mapAll(gradovi, GradDto.class);
    }

    @Override
    public Page<GradDto> findAll(Pageable pageable) {
        Page<Grad> gradPage = gradRepository.findAll(pageable);
        List<GradDto> gradovi = ObjectMapperUtils.mapAll(gradPage.getContent(), GradDto.class);
        return new PageImpl(gradovi, pageable, gradPage.getTotalElements());
    }

    @Override
    public Page<GradDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<Grad> gradPage = gradRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<GradDto> gradovi = ObjectMapperUtils.mapAll(gradPage.getContent(), GradDto.class);
        return new PageImpl(gradovi, queryCriteriaDto.getPageable(), gradPage.getTotalElements());
    }

    @Override
    public List<GradDto> findAll(String text, String... columns) {
        List<Grad> gradovi = gradRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(gradovi, GradDto.class);
    }

    @Override
    public List<GradDto> findAllByDrzavaJeDomovinaTrue(String text, String... columns) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(1, SearchOperation.EQUAL, "drzava.jeDomovina"), Operator.AND))
                .and(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));

        List<Grad> gradovi = gradRepository.findAll(specification);
        return ObjectMapperUtils.mapAll(gradovi, GradDto.class);
    }
}
