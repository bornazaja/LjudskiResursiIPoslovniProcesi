package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PoslovniPartnerServiceImpl implements PoslovniPartnerService {

    @Autowired
    private PoslovniPartnerRepository poslovniPartnerRepository;

    @Override
    public void save(PoslovniPartnerDto poslovniPartnerDto) {
        PoslovniPartner poslovniPartner = ObjectMapperUtils.map(poslovniPartnerDto, PoslovniPartner.class);
        poslovniPartnerRepository.save(poslovniPartner);
    }

    @Override
    public void delete(Integer id) {
        poslovniPartnerRepository.deleteById(id);
    }

    @Override
    public PoslovniPartnerDto findById(Integer id) {
        PoslovniPartner poslovniPartner = poslovniPartnerRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(poslovniPartner, PoslovniPartnerDto.class);
    }

    @Override
    public List<PoslovniPartnerDto> findAll() {
        List<PoslovniPartner> poslovniPartneri = (List<PoslovniPartner>) poslovniPartnerRepository.findAll();
        return ObjectMapperUtils.mapAll(poslovniPartneri, PoslovniPartnerDto.class);
    }

    @Override
    public Boolean existsByOibAndIdPoslovniPartnerNot(String oib, Integer idPoslovniPartner) {
        return poslovniPartnerRepository.existsByOibAndIdPoslovniPartnerNot(oib, idPoslovniPartner);
    }

    @Override
    public Boolean existsByEmailAndIdPoslovniPartnerNot(String email, Integer idPoslovniPartner) {
        return poslovniPartnerRepository.existsByEmailAndIdPoslovniPartnerNot(email, idPoslovniPartner);
    }

    @Override
    public Boolean existsByOib(String oib) {
        return poslovniPartnerRepository.existsByOib(oib);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return poslovniPartnerRepository.existsByEmail(email);
    }

    @Override
    public Long count() {
        return poslovniPartnerRepository.count();
    }

    @Override
    public Page<PoslovniPartnerDto> findAll(Pageable pageable) {
        Page<PoslovniPartner> poslovniPartnerPage = poslovniPartnerRepository.findAll(pageable);
        List<PoslovniPartnerDto> poslovniPartneri = ObjectMapperUtils.mapAll(poslovniPartnerPage.getContent(), PoslovniPartnerDto.class);
        return new PageImpl(poslovniPartneri, pageable, poslovniPartnerPage.getTotalElements());
    }

    @Override
    public Page<PoslovniPartnerDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<PoslovniPartner> poslovniPartnerPage = poslovniPartnerRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<PoslovniPartnerDto> poslovniPartneri = ObjectMapperUtils.mapAll(poslovniPartnerPage.getContent(), PoslovniPartnerDto.class);
        return new PageImpl(poslovniPartneri, queryCriteriaDto.getPageable(), poslovniPartnerPage.getTotalElements());
    }

    @Override
    public List<PoslovniPartnerDto> findAll(String text, String... columns) {
        List<PoslovniPartnerDto> poslovniPartenri = poslovniPartnerRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(poslovniPartenri, PoslovniPartnerDto.class);
    }
}
