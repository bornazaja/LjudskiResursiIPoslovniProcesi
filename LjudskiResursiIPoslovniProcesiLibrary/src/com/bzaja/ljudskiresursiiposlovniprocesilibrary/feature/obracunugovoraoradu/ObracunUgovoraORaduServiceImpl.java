package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
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
public class ObracunUgovoraORaduServiceImpl implements ObracunUgovoraORaduService {

    @Autowired
    private ObracunUgovoraORaduRepository obracunUgovoraORaduRepository;

    @Autowired
    private ObracunUgovoraORaduMaker obracunUgovoraORaduMaker;

    @Override
    public void save(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        ObracunUgovoraORadu obracunUgovoraORadu = obracunUgovoraORaduMaker.make(obracunUgovoraORaduDto);
        obracunUgovoraORaduRepository.save(obracunUgovoraORadu);
    }

    @Override
    public void delete(Integer id) {
        obracunUgovoraORaduRepository.deleteById(id);
    }

    @Override
    public ObracunUgovoraORaduDto findById(Integer id) {
        ObracunUgovoraORadu obracunUgovoraORadu = obracunUgovoraORaduRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(obracunUgovoraORadu, ObracunUgovoraORaduDto.class);
    }

    @Override
    public List<ObracunUgovoraORaduDto> findAll() {
        List<ObracunUgovoraORadu> obracuniUgovoraORadu = (List<ObracunUgovoraORadu>) obracunUgovoraORaduRepository.findAll();
        return ObjectMapperUtils.mapAll(obracuniUgovoraORadu, ObracunUgovoraORaduDto.class);
    }

    @Override
    public Page<ObracunUgovoraORaduDto> findAll(Pageable pageable) {
        Page<ObracunUgovoraORadu> obracunUgovoraORaduPage = obracunUgovoraORaduRepository.findAll(pageable);
        List<ObracunUgovoraORaduDto> obracuniGovoraORadu = ObjectMapperUtils.mapAll(obracunUgovoraORaduPage.getContent(), ObracunUgovoraORaduDto.class);
        return new PageImpl(obracuniGovoraORadu, pageable, obracunUgovoraORaduPage.getTotalElements());
    }

    @Override
    public Page<ObracunUgovoraORaduDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<ObracunUgovoraORadu> obracunUgovoraORaduPage = obracunUgovoraORaduRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<ObracunUgovoraORaduDto> obracuniUgovoraORadu = ObjectMapperUtils.mapAll(obracunUgovoraORaduPage.getContent(), ObracunUgovoraORaduDto.class);
        return new PageImpl(obracuniUgovoraORadu, queryCriteriaDto.getPageable(), obracunUgovoraORaduPage.getTotalElements());
    }
}
