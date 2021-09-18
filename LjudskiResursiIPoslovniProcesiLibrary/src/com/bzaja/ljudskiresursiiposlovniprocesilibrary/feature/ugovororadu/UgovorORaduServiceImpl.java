package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu;

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
public class UgovorORaduServiceImpl implements UgovorORaduService {

    @Autowired
    private UgovorORaduRepository ugovorORaduRepository;

    @Override
    public void save(UgovorORaduDto ugovorORaduDto) {
        UgovorORadu ugovorORadu = ObjectMapperUtils.map(ugovorORaduDto, UgovorORadu.class);
        ugovorORaduRepository.save(ugovorORadu);
    }

    @Override
    public void delete(Integer id) {
        ugovorORaduRepository.deleteById(id);
    }

    @Override
    public UgovorORaduDto findById(Integer id) {
        UgovorORadu ugovorORadu = ugovorORaduRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(ugovorORadu, UgovorORaduDto.class);
    }

    @Override
    public List<UgovorORaduDto> findAll() {
        List<UgovorORadu> ugovoriORadu = (List<UgovorORadu>) ugovorORaduRepository.findAll();
        return ObjectMapperUtils.mapAll(ugovoriORadu, UgovorORaduDto.class);
    }

    @Override
    public List<UgovorORaduDto> findAllInPeriodDatumOdAndDatumDo(LocalDate datumOd, LocalDate datumDo) {
        List<UgovorORadu> ugovoriORadu = ugovorORaduRepository.findAllInPeriodDatumOdAndDatumDo(datumOd, datumDo);
        return ObjectMapperUtils.mapAll(ugovoriORadu, UgovorORaduDto.class);
    }

    @Override
    public Page<UgovorORaduDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<UgovorORadu> ugovorORaduPage = ugovorORaduRepository.findAll(specification, pageable);
        List<UgovorORaduDto> ugovoriORadu = ObjectMapperUtils.mapAll(ugovorORaduPage.getContent(), UgovorORaduDto.class);
        return new PageImpl(ugovoriORadu, pageable, ugovorORaduPage.getTotalElements());
    }

    @Override
    public Page<UgovorORaduDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<UgovorORadu> ugovorORaduPage = ugovorORaduRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<UgovorORaduDto> ugovoriORadu = ObjectMapperUtils.mapAll(ugovorORaduPage.getContent(), UgovorORaduDto.class);
        return new PageImpl(ugovoriORadu, queryCriteriaDto.getPageable(), ugovorORaduPage.getTotalElements());
    }

    @Override
    public List<UgovorORaduDto> findAll(Integer idZaposlenik) {
        List<UgovorORadu> ugovoriORadu = ugovorORaduRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(ugovoriORadu, UgovorORaduDto.class);
    }
}
