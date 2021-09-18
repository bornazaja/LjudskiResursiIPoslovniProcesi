package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import java.util.Set;

public interface ObracunUgovoraCommonMaker {

    ObracunUgovora make(ObracunUgovora obracunUgovora, Set<IsplatnaLista> isplatneListe);
}
