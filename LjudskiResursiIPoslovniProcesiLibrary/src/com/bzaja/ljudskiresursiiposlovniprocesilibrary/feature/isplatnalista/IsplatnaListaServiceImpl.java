package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

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
public class IsplatnaListaServiceImpl implements IsplatnaListaService {

    @Autowired
    private IsplatnaListaRepository isplatnaListaRepository;

    @Override
    public IsplatnaListaDto findById(Integer id) {
        IsplatnaLista isplatnaLista = isplatnaListaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(isplatnaLista, IsplatnaListaDto.class);
    }

    @Override
    public List<IsplatnaListaDto> findAllByIdObracunUgovora(Integer idObracunUgovora) {
        List<IsplatnaLista> isplatneListe = isplatnaListaRepository.findAllByObracunUgovoraIdObracunUgovora(idObracunUgovora);
        return ObjectMapperUtils.mapAll(isplatneListe, IsplatnaListaDto.class);
    }

    @Override
    public Page<IsplatnaListaDto> findAllByIdObracunUgovora(Integer idObracunUgovora, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idObracunUgovora, SearchOperation.EQUAL, "obracunUgovora.idObracunUgovora"), Operator.AND));
        Page<IsplatnaLista> isplatnaListaPage = isplatnaListaRepository.findAll(specification, pageable);
        List<IsplatnaListaDto> isplatneListe = ObjectMapperUtils.mapAll(isplatnaListaPage.getContent(), IsplatnaListaDto.class);
        return new PageImpl(isplatneListe, pageable, isplatnaListaPage.getTotalElements());
    }

    @Override
    public Page<IsplatnaListaDto> findAllByIdObracunUgovora(Integer idObracunUgovora, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idObracunUgovora, SearchOperation.EQUAL, "obracunUgovora.idObracunUgovora"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<IsplatnaLista> isplatnaListaPage = isplatnaListaRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<IsplatnaListaDto> isplatneListe = ObjectMapperUtils.mapAll(isplatnaListaPage.getContent(), IsplatnaListaDto.class);
        return new PageImpl(isplatneListe, queryCriteriaDto.getPageable(), isplatnaListaPage.getTotalElements());
    }

    @Override
    public Page<IsplatnaListaDto> findAllByIdZaposlenik(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "ugovor.zaposlenik.idZaposlenik"), Operator.AND));
        Page<IsplatnaLista> isplatnaListaPage = isplatnaListaRepository.findAll(specification, pageable);
        List<IsplatnaListaDto> isplatneListe = ObjectMapperUtils.mapAll(isplatnaListaPage.getContent(), IsplatnaListaDto.class);
        return new PageImpl(isplatneListe, pageable, isplatnaListaPage.getTotalElements());
    }

    @Override
    public Page<IsplatnaListaDto> findAllByIdZaposlenik(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "ugovor.zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<IsplatnaLista> isplatnaListaPage = isplatnaListaRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<IsplatnaListaDto> isplatneListe = ObjectMapperUtils.mapAll(isplatnaListaPage.getContent(), IsplatnaListaDto.class);
        return new PageImpl(isplatneListe, queryCriteriaDto.getPageable(), isplatnaListaPage.getTotalElements());
    }

    @Override
    public List<IsplatnaListaDto> findAllByIdZaposlenik(Integer idZaposlenik) {
        List<IsplatnaLista> isplatneListe = isplatnaListaRepository.findAllByUgovorZaposlenikIdZaposlenik(idZaposlenik);
        return ObjectMapperUtils.mapAll(isplatneListe, IsplatnaListaDto.class);
    }
}
