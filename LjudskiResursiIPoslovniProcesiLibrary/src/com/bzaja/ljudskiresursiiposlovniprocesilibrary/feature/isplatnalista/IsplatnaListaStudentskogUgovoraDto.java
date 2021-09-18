package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import lombok.Data;

@Data
public class IsplatnaListaStudentskogUgovoraDto {

    private ZaposlenikBasicDetailsDto zaposlenikBasicDetails;
    private StudentskiUgovorDetailsDto studentskiUgovorDetails;
    private DavanjeResultListDto davanjeResultList;
    private DodatakResultListDto dodatakResultList;
    private ObustavaResultListDto obustavaResultList;
    private PrekovremeniRadResultListDto prekovremeniRadResultList;
    private Double ukupanTrosak;
    private Double stopaPoreza;
    private Double stopaPrireza;
    private Double iznosPoreza;
    private Double iznosPrireza;
    private Double porezIPrirezUkupno;
    private Double neto;
    private Double iznosZaIsplatu;
}
