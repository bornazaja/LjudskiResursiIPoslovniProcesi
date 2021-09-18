package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import lombok.Data;

@Data
public class IsplatnaListaUgovoraORaduDto {

    private ZaposlenikBasicDetailsDto zaposlenikBasicDetails;
    private UgovorORaduDetailsDto ugovorORaduDetails;
    private DavanjeResultListDto davanjeResultList;
    private Double ukupanTrosak;
    private Double bruto;
    private Double dohodak;
    private OlaksicaResultListDto olaksicaResultList;
    private Double osobniOdbitak;
    private DodatakResultListDto dodatakResultList;
    private ObustavaResultListDto obustavaResultList;
    private PrekovremeniRadResultListDto prekovremeniRadResultList;
    private Double ukupnoPorez;
    private Double stopaPoreza;
    private Double stopaPrireza;
    private Double iznosPoreza;
    private Double iznosPrireza;
    private Double porezIPrirezUkupno;
    private Double neto;
    private Double iznosZaIsplatu;
}
