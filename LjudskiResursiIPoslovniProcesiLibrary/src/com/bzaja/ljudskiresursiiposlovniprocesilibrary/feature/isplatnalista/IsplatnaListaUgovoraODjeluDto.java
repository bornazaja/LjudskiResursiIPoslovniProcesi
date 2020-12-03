package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import lombok.Data;

@Data
public class IsplatnaListaUgovoraODjeluDto {

    private ZaposlenikBasicDetailsDto zaposlenikBasicDetails;
    private UgovorODjeluDetailsDto ugovorODjeluDetails;
    private DavanjeResultListDto davanjeResultList;
    private DodatakResultListDto dodatakResultList;
    private ObustavaResultListDto obustavaResultList;
    private Double ukupanTrosak;
    private Double iznosPausalnogPriznatogTroska;
    private Double bruto;
    private Double dohodak;
    private Double ukupnoPorez;
    private Double stopaPoreza;
    private Double stopaPrireza;
    private Double iznosPoreza;
    private Double iznosPrireza;
    private Double porezIPrirezUkupno;
    private Double neto;
    private Double iznosZaIsplatu;
}
