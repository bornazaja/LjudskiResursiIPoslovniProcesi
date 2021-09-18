package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ParametriZaObracunPlaceServiceImpl implements ParametriZaObracunPlaceService {

    @Autowired
    private ParametriZaObracunPlaceRepository parametriZaObracunPlaceRepository;

    @Override
    public void save(ParametriZaObracunPlaceDto parametriZaObracunPlaceDto) {
        ParametriZaObracunPlace parametriZaObracunPlace = ObjectMapperUtils.map(parametriZaObracunPlaceDto, ParametriZaObracunPlace.class);
        parametriZaObracunPlaceRepository.save(parametriZaObracunPlace);
    }

    @Override
    public ParametriZaObracunPlaceDto findFirst() {
        ParametriZaObracunPlace parametriZaObracunPlace = parametriZaObracunPlaceRepository.findFirstByOrderByIdParametriZaObracunPlaceAsc();
        return ObjectMapperUtils.map(parametriZaObracunPlace, ParametriZaObracunPlaceDto.class);
    }

}
