package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica;

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
public class OlaksicaServiceImpl implements OlaksicaService {

    @Autowired
    private OlaksicaRepository olaksicaRepository;

    @Override
    public void save(OlaksicaDto olaksicaDto) {
        Olaksica olaksica = ObjectMapperUtils.map(olaksicaDto, Olaksica.class);
        olaksicaRepository.save(olaksica);
    }

    @Override
    public void delete(Integer id) {
        olaksicaRepository.deleteById(id);
    }

    @Override
    public OlaksicaDto findById(Integer id) {
        Olaksica olaksica = olaksicaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(olaksica, OlaksicaDto.class);
    }

    @Override
    public List<OlaksicaDto> findAll() {
        List<Olaksica> olaksice = (List<Olaksica>) olaksicaRepository.findAll();
        return ObjectMapperUtils.mapAll(olaksice, OlaksicaDto.class);
    }

    @Override
    public List<OlaksicaDto> findAllInPeriodDatumOdAndDatumDoByZaposlenikId(LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<Olaksica> olaksice = (List<Olaksica>) olaksicaRepository.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        return ObjectMapperUtils.mapAll(olaksice, OlaksicaDto.class);
    }

    @Override
    public Page<OlaksicaDto> findAll(Integer idZaposelnik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposelnik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<OlaksicaDto> olaksicaPage = olaksicaRepository.findAll(specification, pageable);
        List<OlaksicaDto> olaksice = ObjectMapperUtils.mapAll(olaksicaPage.getContent(), OlaksicaDto.class);
        return new PageImpl(olaksice, pageable, olaksicaPage.getTotalElements());
    }

    @Override
    public Page<OlaksicaDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<Olaksica> olaksicaPage = olaksicaRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<OlaksicaDto> olaksice = ObjectMapperUtils.mapAll(olaksicaPage.getContent(), OlaksicaDto.class);
        return new PageImpl(olaksice, queryCriteriaDto.getPageable(), olaksicaPage.getTotalElements());
    }

    @Override
    public List<OlaksicaDto> findAll(Integer idZaposlenik) {
        List<Olaksica> olaksice = olaksicaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(olaksice, OlaksicaDto.class);
    }

    @Override
    public Long countAktvneOlaksiceByZaposlenikId(Integer idZaposlenik) {
        return olaksicaRepository.countAktivneOlaksiceByZaposlenikId(idZaposlenik);
    }
}
