package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PovijestPrijavaServiceImpl implements PovijestPrijavaService {

    @Autowired
    private PovijestPrijavaRepository povijestPrijavaRepository;

    @Override
    public PovijestPrijavaDto findById(Integer id) {
        PovijestPrijava povijestPrijava = povijestPrijavaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(povijestPrijava, PovijestPrijavaDto.class);
    }

    @Override
    public List<PovijestPrijavaDto> findAll() {
        List<PovijestPrijava> povijestPrijavaList = (List<PovijestPrijava>) povijestPrijavaRepository.findAll();
        return ObjectMapperUtils.mapAll(povijestPrijavaList, PovijestPrijavaDto.class);
    }

    @Override
    public Long count() {
        return povijestPrijavaRepository.count();
    }

    @Override
    public Long countByZaposlenikId(Integer idZaposlenik) {
        return povijestPrijavaRepository.countByZaposlenikIdZaposlenik(idZaposlenik);
    }

    @Override
    public List<PovijestPrijavaDto> findRecentPovijestPrijava(Integer n) {
        List<PovijestPrijava> povijestPrijavaList = povijestPrijavaRepository.findRecentPovijestPrijava(PageRequest.of(0, n));
        return ObjectMapperUtils.mapAll(povijestPrijavaList, PovijestPrijavaDto.class);
    }

    @Override
    public List<PovijestPrijavaStatisticsDto> findRecentDnevniBrojPrijava(Integer n) {
        return povijestPrijavaRepository.findRecentDnevniBrojPrijava(PageRequest.of(0, n));
    }

    @Override
    public Page<PovijestPrijavaDto> findAll(Pageable pageable) {
        Page<PovijestPrijava> povijestPrijavaPage = povijestPrijavaRepository.findAll(pageable);
        List<PovijestPrijavaDto> povijestPrijavaList = ObjectMapperUtils.mapAll(povijestPrijavaPage.getContent(), PovijestPrijavaDto.class);
        return new PageImpl(povijestPrijavaList, pageable, povijestPrijavaPage.getTotalElements());
    }

    @Override
    public Page<PovijestPrijavaDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<PovijestPrijava> povijestPrijavaPage = povijestPrijavaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<PovijestPrijavaDto> povijestPrijavaList = ObjectMapperUtils.mapAll(povijestPrijavaPage.getContent(), PovijestPrijavaDto.class);
        return new PageImpl(povijestPrijavaList, queryCriteriaDto.getPageable(), povijestPrijavaPage.getTotalElements());
    }
}
