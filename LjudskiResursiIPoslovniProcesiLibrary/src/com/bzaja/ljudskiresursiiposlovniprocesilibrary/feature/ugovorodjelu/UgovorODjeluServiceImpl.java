package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu;

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
public class UgovorODjeluServiceImpl implements UgovorODjeluService {

    @Autowired
    private UgovorODjeluRepository ugovorODjeluRepository;

    @Override
    public void save(UgovorODjeluDto ugovorODjeluDto) {
        UgovorODjelu ugovorODjelu = ObjectMapperUtils.map(ugovorODjeluDto, UgovorODjelu.class);
        ugovorODjeluRepository.save(ugovorODjelu);
    }

    @Override
    public void delete(Integer id) {
        ugovorODjeluRepository.deleteById(id);
    }

    @Override
    public UgovorODjeluDto findById(Integer id) {
        UgovorODjelu ugovorODjelu = ugovorODjeluRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(ugovorODjelu, UgovorODjeluDto.class);
    }

    @Override
    public List<UgovorODjeluDto> findAll() {
        List<UgovorODjelu> ugovoriODjelu = (List<UgovorODjelu>) ugovorODjeluRepository.findAll();
        return ObjectMapperUtils.mapAll(ugovoriODjelu, UgovorODjeluDto.class);
    }

    @Override
    public List<UgovorODjeluDto> findByIdUgovorIn(List<Integer> ids) {
        List<UgovorODjelu> ugovoriODjelu = ugovorODjeluRepository.findByIdUgovorIn(ids);
        return ObjectMapperUtils.mapAll(ugovoriODjelu, UgovorODjeluDto.class);
    }

    @Override
    public Page<UgovorODjeluDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<UgovorODjelu> ugovorODjeluPage = ugovorODjeluRepository.findAll(specification, pageable);
        List<UgovorODjeluDto> ugovoriODjelu = ObjectMapperUtils.mapAll(ugovorODjeluPage.getContent(), UgovorODjeluDto.class);
        return new PageImpl(ugovoriODjelu, pageable, ugovorODjeluPage.getTotalElements());
    }

    @Override
    public Page<UgovorODjeluDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<UgovorODjelu> ugovorODjeluPage = ugovorODjeluRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<UgovorODjeluDto> ugovoriODjelu = ObjectMapperUtils.mapAll(ugovorODjeluPage.getContent(), UgovorODjeluDto.class);
        return new PageImpl(ugovoriODjelu, queryCriteriaDto.getPageable(), ugovorODjeluPage.getTotalElements());
    }

    @Override
    public List<UgovorODjeluDto> findAll(Integer idZaposlenik) {
        List<UgovorODjelu> ugovoriODjelu = ugovorODjeluRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(ugovoriODjelu, UgovorODjeluDto.class);
    }

    @Override
    public List<UgovorODjeluDto> findAllNeObraucunateUgovore(String text, String... columns) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(0, SearchOperation.EQUAL, "jeObracunat"), Operator.AND))
                .and(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));

        List<UgovorODjelu> ugovoriODjelu = ugovorODjeluRepository.findAll(specification);
        return ObjectMapperUtils.mapAll(ugovoriODjelu, UgovorODjeluDto.class);
    }
}
