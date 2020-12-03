package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UgovorODjeluDetailsDto {

    private String vrstaUgovora;
    private String radniOdnos;
    private String radnoMjesto;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private Double brutoIznos;
    private Double StopaPausalnogPriznatogTroska;
}
