package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorDto;
import java.util.List;
import lombok.Data;

@Data
public class IsplatnaListaIzvjestajDto {

    private UgovorDto ugovor;
    private PrebivalisteDto prebivaliste;
    private List<DavanjeDto> davanja;
    private Double ukupanIznosDavanja;
    private List<OlaksicaDto> olaksice;
    private Double ukupanIznosOlaksica;
    private List<DodatakDto> dodatci;
    private Double ukupanIznosDodataka;
    private List<ObustavaDto> obustave;
    private Double ukupanIznosObustave;
    private List<PrekovremeniRadDto> prekovremeniRadovi;
    private Double ukupanIznosPrekovremenihRadova;
    private Double ukupniTrosak;
    private Double brutoDohodak;
    private Double dohodak;
    private Double osobniOdbitak;
    private Double porez;
    private Double prirez;
    private Double neto;
}
