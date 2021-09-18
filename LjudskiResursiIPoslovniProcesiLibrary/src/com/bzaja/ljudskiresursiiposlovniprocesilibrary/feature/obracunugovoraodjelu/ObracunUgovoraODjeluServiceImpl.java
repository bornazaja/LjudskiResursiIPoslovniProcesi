package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjelu;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluRepository;
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
public class ObracunUgovoraODjeluServiceImpl implements ObracunUgovoraODjeluService {

    @Autowired
    private ObracunUgovoraODjeluRepository obracunUgovoraODjeluRepository;

    @Autowired
    private UgovorODjeluRepository ugovorODjeluRepository;

    @Autowired
    private ObracunUgovoraODjeluMaker obracunUgovoraODjeluMaker;

    @Override
    public void save(AddObracunUgovoraODjeluDto addObracunUgovoraODjeluDto) {
        ObracunUgovoraODjelu obracunUgovoraODjelu = obracunUgovoraODjeluMaker.make(addObracunUgovoraODjeluDto);
        obracunUgovoraODjeluRepository.save(obracunUgovoraODjelu);

        List<UgovorODjelu> ugovoriODjelu = ugovorODjeluRepository.findByIdUgovorIn(addObracunUgovoraODjeluDto.getIdeviUgovora());
        ugovoriODjelu.forEach((ugovorODjelu) -> {
            ugovorODjelu.setJeObracunat(true);
            ugovorODjeluRepository.save(ugovorODjelu);
        });
    }

    @Override
    public void delete(Integer id) {
        List<UgovorODjelu> ugovoriODjelu = ugovorODjeluRepository.findByIsplatneListeObracunUgovoraIdObracunUgovora(id);
        ugovoriODjelu.forEach((ugovorODjelu) -> {
            ugovorODjelu.setJeObracunat(false);
            ugovorODjeluRepository.save(ugovorODjelu);
        });

        obracunUgovoraODjeluRepository.deleteById(id);
    }

    @Override
    public ObracunUgovoraODjeluDto findById(Integer id) {
        ObracunUgovoraODjelu obracunUgovoraODjelu = obracunUgovoraODjeluRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(obracunUgovoraODjelu, ObracunUgovoraODjeluDto.class);
    }

    @Override
    public List<ObracunUgovoraODjeluDto> findAll() {
        List<ObracunUgovoraODjelu> obracuniUgovoraODjelu = (List<ObracunUgovoraODjelu>) obracunUgovoraODjeluRepository.findAll();
        return ObjectMapperUtils.mapAll(obracuniUgovoraODjelu, ObracunUgovoraODjeluDto.class);
    }

    @Override
    public Page<ObracunUgovoraODjeluDto> findAll(Pageable pageable) {
        Page<ObracunUgovoraODjelu> obracunUgovoraODjeluPage = obracunUgovoraODjeluRepository.findAll(pageable);
        List<ObracunUgovoraODjeluDto> obracuniUgovoraODjelu = ObjectMapperUtils.mapAll(obracunUgovoraODjeluPage.getContent(), ObracunUgovoraODjeluDto.class);
        return new PageImpl(obracuniUgovoraODjelu, pageable, obracunUgovoraODjeluPage.getTotalElements());
    }

    @Override
    public Page<ObracunUgovoraODjeluDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<ObracunUgovoraODjelu> obracunUgovoraODjeluPage = obracunUgovoraODjeluRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<ObracunUgovoraODjeluDto> obracuniUgovoraODjelu = ObjectMapperUtils.mapAll(obracunUgovoraODjeluPage.getContent(), ObracunUgovoraODjeluDto.class);
        return new PageImpl(obracuniUgovoraODjelu, queryCriteriaDto.getPageable(), obracunUgovoraODjeluPage.getTotalElements());
    }
}
