package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VrstaTransakcijeServiceImpl implements VrstaTransakcijeService {

    @Autowired
    private VrstaTransakcijeRepository vrstaTransakcijeRepository;

    @Override
    public VrstaTransakcijeDto findById(Integer id) {
        VrstaTransakcije vrstaTransakcije = vrstaTransakcijeRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaTransakcije, VrstaTransakcijeDto.class);
    }

    @Override
    public List<VrstaTransakcijeDto> findAll() {
        List<VrstaTransakcije> vrstaTransakcija = (List<VrstaTransakcije>) vrstaTransakcijeRepository.findAll();
        return ObjectMapperUtils.mapAll(vrstaTransakcija, VrstaTransakcijeDto.class);
    }

    @Override
    public List<VrstaTransakcijeDto> findAll(String text, String... columns) {
        List<VrstaTransakcije> vrsteTransakcija = vrstaTransakcijeRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(vrsteTransakcija, VrstaTransakcijeDto.class);
    }
}
