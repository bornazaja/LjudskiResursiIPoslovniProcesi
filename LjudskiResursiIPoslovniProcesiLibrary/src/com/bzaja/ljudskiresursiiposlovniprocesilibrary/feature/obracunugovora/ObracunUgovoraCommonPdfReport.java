package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.itextpdf.text.pdf.PdfPTable;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;

public interface ObracunUgovoraCommonPdfReport {

    PdfPTable createPodaciOZaposlenikuTable(ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto);

    PdfPTable createDavanjaTable(DavanjeResultListDto davanjeResultList);

    PdfPTable createDodatciTable(DodatakResultListDto dodatakResultList);

    PdfPTable createObustaveTable(ObustavaResultListDto obustavaResultList);

    PdfPTable createPrekovremeniRadoviTable(PrekovremeniRadResultListDto prekovremeniRadResultList);
}
