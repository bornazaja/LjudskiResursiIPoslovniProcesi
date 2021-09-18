package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VrstaUgovoraServiceImpl implements VrstaUgovoraService {

    @Autowired
    private VrstaUgovoraRepository vrstaUgovoraRepository;

    @Override
    public VrstaUgovoraDto findById(Integer id) {
        VrstaUgovora vrstaUgovora = vrstaUgovoraRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(vrstaUgovora, VrstaUgovoraDto.class);
    }

    @Override
    public List<VrstaUgovoraDto> findAll() {
        List<VrstaUgovora> vrsteUgovora = (List<VrstaUgovora>) vrstaUgovoraRepository.findAll();
        return ObjectMapperUtils.mapAll(vrsteUgovora, VrstaUgovoraDto.class);
    }
}
