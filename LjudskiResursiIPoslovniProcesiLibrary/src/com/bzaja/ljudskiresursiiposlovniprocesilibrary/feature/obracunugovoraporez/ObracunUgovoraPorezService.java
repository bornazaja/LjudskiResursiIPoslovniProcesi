package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez;

public interface ObracunUgovoraPorezService {

    Double findStopaByObracunUgovoraIdAndDohodak(Integer idObracunUgovora, Double dohodak);

    Double findStopaByObracunUgovoraId(Integer idObracunUgovora);
}
