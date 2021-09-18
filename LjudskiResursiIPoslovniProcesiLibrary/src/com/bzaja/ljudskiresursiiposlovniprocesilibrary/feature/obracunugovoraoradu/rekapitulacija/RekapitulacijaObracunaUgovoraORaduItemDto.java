package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.rekapitulacija;

import lombok.Data;

@Data
public class RekapitulacijaObracunaUgovoraORaduItemDto {

    private String imeIPrezimeZaposlenika;
    private Double ukupanTrosak;
    private Double bruto;
    private Double dohodak;
    private Double iznosDavanjaNaPlacu;
    private Double iznosDavanjaIzPlace;
    private Double osobniOdbitak;
    private Double iznosPoreza;
    private Double iznosPrireza;
    private Double porezIPrirez;
    private Double neto;
    private Double iznosDodataka;
    private Double iznosObustava;
    private Double iznosPrekovremenihRadova;
    private Double iznosZaIsplatu;
}
