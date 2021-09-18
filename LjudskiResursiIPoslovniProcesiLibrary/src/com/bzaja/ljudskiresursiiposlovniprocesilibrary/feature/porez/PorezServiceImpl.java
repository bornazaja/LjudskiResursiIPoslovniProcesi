package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez;

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
public class PorezServiceImpl implements PorezService {

    @Autowired
    private PorezRepository porezRepository;

    @Override
    public void save(PorezDto porezDto) {
        Porez porez = ObjectMapperUtils.map(porezDto, Porez.class);
        porezRepository.save(porez);
    }

    @Override
    public void delete(Integer id) {
        porezRepository.deleteById(id);
    }

    @Override
    public PorezDto findById(Integer id) {
        Porez porez = porezRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(porez, PorezDto.class);
    }

    @Override
    public List<PorezDto> findAll() {
        List<Porez> porezi = (List<Porez>) porezRepository.findAll();
        return ObjectMapperUtils.mapAll(porezi, PorezDto.class);
    }

    @Override
    public Page<PorezDto> findAll(Pageable pageable) {
        Page<Porez> porezPage = porezRepository.findAll(pageable);
        List<PorezDto> porezi = ObjectMapperUtils.mapAll(porezPage.getContent(), PorezDto.class);
        return new PageImpl(porezi, pageable, porezPage.getTotalElements());
    }

    @Override
    public Page<PorezDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<Porez> porezPage = porezRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<PorezDto> porezi = ObjectMapperUtils.mapAll(porezPage.getContent(), PorezDto.class);
        return new PageImpl(porezi, queryCriteriaDto.getPageable(), porezPage.getTotalElements());
    }

    @Override
    public Double findStopaByOsnovicaAndVrstaPorezaId(Double osnovica, Integer idVrstaPoreza) {
        return porezRepository.findStopaByOsnovicaAndVrstaPorezaId(osnovica, idVrstaPoreza);
    }

    @Override
    public List<PorezDto> findAllByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrue(Integer idVrstaPoreza) {
        List<Porez> porezi = porezRepository.findAllByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrue(idVrstaPoreza);
        return ObjectMapperUtils.mapAll(porezi, PorezDto.class);
    }

    @Override
    public PorezDto findFirstByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrueOrderByStopaAsc(Integer idVrstaPoreza) {
        Porez porez = porezRepository.findFirstByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrueOrderByStopaAsc(idVrstaPoreza);
        return ObjectMapperUtils.map(porez, PorezDto.class);
    }
}
