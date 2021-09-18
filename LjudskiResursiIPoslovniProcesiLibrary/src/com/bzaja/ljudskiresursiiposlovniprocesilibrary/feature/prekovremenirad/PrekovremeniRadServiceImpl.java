package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.time.LocalDate;
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
public class PrekovremeniRadServiceImpl implements PrekovremeniRadService {

    @Autowired
    private PrekovremeniRadRepository prekovremeniRadRepository;

    @Override
    public void save(PrekovremeniRadDto prekovremeniRadDto) {
        PrekovremeniRad prekovremeniRad = ObjectMapperUtils.map(prekovremeniRadDto, PrekovremeniRad.class);
        prekovremeniRadRepository.save(prekovremeniRad);
    }

    @Override
    public void delete(Integer id) {
        prekovremeniRadRepository.deleteById(id);
    }

    @Override
    public PrekovremeniRadDto findById(Integer id) {
        PrekovremeniRad prekovremeniRad = prekovremeniRadRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(prekovremeniRad, PrekovremeniRadDto.class);
    }

    @Override
    public List<PrekovremeniRadDto> findAll() {
        List<PrekovremeniRad> prekovremeniRadovi = (List<PrekovremeniRad>) prekovremeniRadRepository.findAll();
        return ObjectMapperUtils.mapAll(prekovremeniRadovi, PrekovremeniRadDto.class);
    }

    @Override
    public List<PrekovremeniRadDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<PrekovremeniRad> prekovremeniRadovi = (List<PrekovremeniRad>) prekovremeniRadRepository.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        return ObjectMapperUtils.mapAll(prekovremeniRadovi, PrekovremeniRadDto.class);
    }

    @Override
    public Page<PrekovremeniRadDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<PrekovremeniRad> prekovremeniRadPage = prekovremeniRadRepository.findAll(specification, pageable);
        List<PrekovremeniRadDto> prekovremeniRadovi = ObjectMapperUtils.mapAll(prekovremeniRadPage.getContent(), PrekovremeniRadDto.class);
        return new PageImpl(prekovremeniRadovi, pageable, prekovremeniRadPage.getTotalElements());
    }

    @Override
    public Page<PrekovremeniRadDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<PrekovremeniRad> prekovremeniRadPage = prekovremeniRadRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<PrekovremeniRadDto> prekovremeniRadovi = ObjectMapperUtils.mapAll(prekovremeniRadPage.getContent(), PrekovremeniRadDto.class);
        return new PageImpl(prekovremeniRadovi, queryCriteriaDto.getPageable(), prekovremeniRadPage.getTotalElements());
    }

    @Override
    public List<PrekovremeniRadDto> findAll(Integer idZaposlenik) {
        List<PrekovremeniRad> prekovremeniRadovi = prekovremeniRadRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(prekovremeniRadovi, PrekovremeniRadDto.class);
    }

    @Override
    public Long countAktivniPrekovremeniRadoviByZaposlenikId(Integer idZaposlenik) {
        return prekovremeniRadRepository.countAktivniPrekovremeniRadoviByZaposlenikId(idZaposlenik);
    }
}
