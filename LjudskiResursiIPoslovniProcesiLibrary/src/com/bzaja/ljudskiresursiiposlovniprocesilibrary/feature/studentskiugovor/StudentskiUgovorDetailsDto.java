package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor;

import java.time.LocalDate;
import lombok.Data;

@Data
public class StudentskiUgovorDetailsDto {

    private String vrstaUgovora;
    private String radniOdnos;
    private String radnoMjesto;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private Double brojOdradjenihSati;
    private Double cijenaPoSatu;
    private Double dosadZaradjeniIznosUOvojGodini;
}
