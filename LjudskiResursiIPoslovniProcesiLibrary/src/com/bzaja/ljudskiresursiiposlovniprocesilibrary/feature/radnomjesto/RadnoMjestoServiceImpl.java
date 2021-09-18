package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto;

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
public class RadnoMjestoServiceImpl implements RadnoMjestoService {

    @Autowired
    private RadnoMjestoRepository radnoMjestoRepository;

    @Override
    public void save(RadnoMjestoDto radnoMjestoDto) {
        RadnoMjesto radnoMjesto = ObjectMapperUtils.map(radnoMjestoDto, RadnoMjesto.class);
        radnoMjestoRepository.save(radnoMjesto);
    }

    @Override
    public void delete(Integer id) {
        radnoMjestoRepository.deleteById(id);
    }

    @Override
    public RadnoMjestoDto findById(Integer id) {
        RadnoMjesto radnoMjesto = radnoMjestoRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(radnoMjesto, RadnoMjestoDto.class);
    }

    @Override
    public List<RadnoMjestoDto> findAll() {
        List<RadnoMjesto> radnaMjesta = (List<RadnoMjesto>) radnoMjestoRepository.findAll();
        return ObjectMapperUtils.mapAll(radnaMjesta, RadnoMjestoDto.class);
    }

    @Override
    public Page<RadnoMjestoDto> findAll(Pageable pageable) {
        Page<RadnoMjesto> radnoMjestoPage = radnoMjestoRepository.findAll(pageable);
        List<RadnoMjestoDto> radnaMjesta = ObjectMapperUtils.mapAll(radnoMjestoPage.getContent(), RadnoMjestoDto.class);
        return new PageImpl(radnaMjesta, pageable, radnoMjestoPage.getTotalElements());
    }

    @Override
    public Page<RadnoMjestoDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<RadnoMjesto> radnoMjestoPage = radnoMjestoRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<RadnoMjestoDto> radnaMjesta = ObjectMapperUtils.mapAll(radnoMjestoPage.getContent(), RadnoMjestoDto.class);
        return new PageImpl(radnaMjesta, queryCriteriaDto.getPageable(), radnoMjestoPage.getTotalElements());
    }

    @Override
    public List<RadnoMjestoDto> findAll(String text, String... columns) {
        List<RadnoMjesto> radnaMjesta = radnoMjestoRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(radnaMjesta, RadnoMjestoDto.class);
    }

    @Override
    public Long count() {
        return radnoMjestoRepository.count();
    }
}
