package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VrstaObracunaServiceImpl implements VrstaObracunaService {

    @Autowired
    private VrstaObracunaRepository vrstaObracunaRepository;

    @Override
    public VrstaObracunaDto findById(Integer id) {
        VrstaObracuna vrstaObracuna = vrstaObracunaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaObracuna, VrstaObracunaDto.class);
    }

    @Override
    public List<VrstaObracunaDto> findAll() {
        List<VrstaObracuna> vrsteObracuna = (List<VrstaObracuna>) vrstaObracunaRepository.findAll();
        return ObjectMapperUtils.mapAll(vrsteObracuna, VrstaObracunaDto.class);
    }

}
