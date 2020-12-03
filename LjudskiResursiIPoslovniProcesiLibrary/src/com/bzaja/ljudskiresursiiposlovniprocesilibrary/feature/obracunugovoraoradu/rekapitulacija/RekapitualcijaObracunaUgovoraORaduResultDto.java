package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.rekapitulacija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import java.util.List;
import lombok.Data;

@Data
public class RekapitualcijaObracunaUgovoraORaduResultDto extends ObracunUgovoraORaduDto{

    private PodaciOTvrtkiDto podaciOTvrtki;
    private List<RekapitulacijaObracunaUgovoraORaduItemDto> rekapitulacijaObracunaUgovoraORaduItems;
    private Double ukupnoUkupanTrosak;
    private Double ukupanBruto;
    private Double ukupanDohodak;
    private Double ukupanIznosDavanjaNaPlacu;
    private Double ukupanIznosDavanjaIzPlace;
    private Double ukupanOsobniOdbitak;
    private Double ukupanIznosPoreza;
    private Double ukupanIznosPrireza;
    private Double ukupanPorezIPrirez;
    private Double ukupanNeto;
    private Double ukupanIznosDodataka;
    private Double ukupanIznosObustava;
    private Double ukupanIznosPrekovremenihRadova;
    private Double ukupanIznosZaIsplatu;
}
