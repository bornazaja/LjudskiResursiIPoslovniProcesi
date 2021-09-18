package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava;

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
public class ObustavaServiceImpl implements ObustavaService {

    @Autowired
    private ObustavaRepository obustavaRepository;

    @Override
    public void save(ObustavaDto obustavaDto) {
        Obustava obustava = ObjectMapperUtils.map(obustavaDto, Obustava.class);
        obustavaRepository.save(obustava);
    }

    @Override
    public void delete(Integer id) {
        obustavaRepository.deleteById(id);
    }

    @Override
    public ObustavaDto findById(Integer id) {
        Obustava obustava = obustavaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(obustava, ObustavaDto.class);
    }

    @Override
    public List<ObustavaDto> findAll() {
        List<Obustava> obustave = (List<Obustava>) obustavaRepository.findAll();
        return ObjectMapperUtils.mapAll(obustave, ObustavaDto.class);
    }

    @Override
    public Page<ObustavaDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<Obustava> obustavaPage = obustavaRepository.findAll(specification, pageable);
        List<ObustavaDto> obustave = ObjectMapperUtils.mapAll(obustavaPage.getContent(), ObustavaDto.class);
        return new PageImpl(obustave, pageable, obustavaPage.getTotalElements());
    }

    @Override
    public Page<ObustavaDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<Obustava> obustavaPage = obustavaRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<ObustavaDto> obustave = ObjectMapperUtils.mapAll(obustavaPage.getContent(), ObustavaDto.class);
        return new PageImpl(obustave, queryCriteriaDto.getPageable(), obustavaPage.getTotalElements());
    }

    @Override
    public List<ObustavaDto> findAll(Integer idZaposlenik) {
        List<Obustava> obustave = obustavaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(obustave, ObustavaDto.class);
    }

    @Override
    public List<ObustavaDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<Obustava> obustave = obustavaRepository.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        return ObjectMapperUtils.mapAll(obustave, ObustavaDto.class);
    }

    @Override
    public Long countAktivneObustaveByZaposlenikId(Integer idZaposlenik) {
        return obustavaRepository.countAktivneObustaveByZaposlenikId(idZaposlenik);
    }
}
