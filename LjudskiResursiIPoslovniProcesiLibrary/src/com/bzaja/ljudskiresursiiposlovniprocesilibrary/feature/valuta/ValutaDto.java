package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ValutaDto {

    private Integer idValuta;
    private String naziv;
    private Integer jedinica;
    private Double srednjiTecaj;
    private LocalDate datumTecaja;
    private Boolean jeAktivna;
}
