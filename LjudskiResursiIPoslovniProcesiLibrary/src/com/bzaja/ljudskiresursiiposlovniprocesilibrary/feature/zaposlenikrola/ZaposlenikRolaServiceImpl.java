package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola;

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
public class ZaposlenikRolaServiceImpl implements ZaposlenikRolaService {

    @Autowired
    private ZaposlenikRolaRepository zaposlenikRolaRepository;

    @Override
    public void save(ZaposlenikRolaDto zaposlenikRolaDto) {
        ZaposlenikRola zaposlenikRola = ObjectMapperUtils.map(zaposlenikRolaDto, ZaposlenikRola.class);
        zaposlenikRolaRepository.save(zaposlenikRola);
    }

    @Override
    public void delete(Integer id) {
        zaposlenikRolaRepository.deleteById(id);
    }

    @Override
    public ZaposlenikRolaDto findById(Integer id) {
        ZaposlenikRola zaposlenikRola = zaposlenikRolaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(zaposlenikRola, ZaposlenikRolaDto.class);
    }

    @Override
    public List<ZaposlenikRolaDto> findAll() {
        List<ZaposlenikRola> zaposlenikRola = (List<ZaposlenikRola>) zaposlenikRolaRepository.findAll();
        return ObjectMapperUtils.mapAll(zaposlenikRola, ZaposlenikRolaDto.class);
    }

    @Override
    public Page<ZaposlenikRolaDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<ZaposlenikRola> zaposlenikRolaPage = zaposlenikRolaRepository.findAll(specification, pageable);
        List<ZaposlenikRolaDto> zaposlenikRolaList = ObjectMapperUtils.mapAll(zaposlenikRolaPage.getContent(), ZaposlenikRolaDto.class);
        return new PageImpl(zaposlenikRolaList, pageable, zaposlenikRolaPage.getTotalElements());
    }

    @Override
    public Page<ZaposlenikRolaDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<ZaposlenikRola> zaposlenikRolaPage = zaposlenikRolaRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<ZaposlenikRolaDto> zaposlenikRolaList = ObjectMapperUtils.mapAll(zaposlenikRolaPage.getContent(), ZaposlenikRolaDto.class);
        return new PageImpl(zaposlenikRolaList, queryCriteriaDto.getPageable(), zaposlenikRolaPage.getTotalElements());
    }

    @Override
    public List<ZaposlenikRolaDto> findAll(Integer idZaposlenik) {
        List<ZaposlenikRola> zaposlenikaRolaList = zaposlenikRolaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(zaposlenikaRolaList, ZaposlenikRolaDto.class);
    }
}
