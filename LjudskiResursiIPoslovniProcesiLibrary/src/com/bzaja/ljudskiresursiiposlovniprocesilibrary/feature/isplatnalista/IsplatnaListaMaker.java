package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import java.time.LocalDate;

public interface IsplatnaListaMaker {

    IsplatnaLista make(ObracunUgovora obracunUgovora, Ugovor ugovor, LocalDate datumOd, LocalDate datumDo);
}
