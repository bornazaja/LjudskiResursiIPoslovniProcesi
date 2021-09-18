package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;

public interface ObracunUgovoraCommonKalkulator {

    DavanjeResultListDto getDavanjeResultList(Integer idIsplatnaLista, Double osnovica);

    DodatakResultListDto getDodatakResultList(Integer idIsplatnaLista);

    ObustavaResultListDto getObustavaResultList(Integer idIsplatnaLista);

    OlaksicaResultListDto getOlaksicaResultList(Integer idIsplatnaLista, Double osnovicaOsobnogOdbitka);

    PrekovremeniRadResultListDto getPrekovremeniRadResultList(Integer idIsplatnaLista, Double cijenaPoSatu);
}
