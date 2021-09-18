package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class TransakcijaResultListDto {

    private PodaciOTvrtkiDto podaciOTvrtki;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private List<TransakcijaReportItemDto> transakcijaReportItems;
    private Double ukupanIznosPrihoda;
    private Double ukupanIznosRashoda;
    private Double profitPrijePorezaIPrireza;
    private Double stopaPoreza;
    private Double stopaPrireza;
    private Double iznosPoreza;
    private Double iznosPrireza;
    private Double porezIPrirezUkupno;
    private Double profitNakonPorezaIPrireza;
    private ValutaDto valuta;
}
