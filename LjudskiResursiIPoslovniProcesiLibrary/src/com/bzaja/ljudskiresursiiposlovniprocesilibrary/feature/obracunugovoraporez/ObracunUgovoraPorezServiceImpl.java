package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ObracunUgovoraPorezServiceImpl implements ObracunUgovoraPorezService {

    @Autowired
    private ObracunUgovoraPorezRepository obracunUgovoraPorezRepository;

    @Override
    public Double findStopaByObracunUgovoraIdAndDohodak(Integer idObracunUgovora, Double dohodak) {
        return obracunUgovoraPorezRepository.findStopaByObracunUgovoraIdAndDohodak(idObracunUgovora, dohodak);
    }

    @Override
    public Double findStopaByObracunUgovoraId(Integer idObracunUgovora) {
        return obracunUgovoraPorezRepository.findStopaByObracunUgovoraId(idObracunUgovora);
    }
}
