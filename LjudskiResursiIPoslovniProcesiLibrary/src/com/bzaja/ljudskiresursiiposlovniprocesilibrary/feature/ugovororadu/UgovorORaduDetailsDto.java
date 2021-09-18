package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UgovorORaduDetailsDto {

    private String vrstaUgovora;
    private String radniOdnos;
    private String radnoMjesto;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private Double brojRadnihSatiTjedno;
    private Double brutoPlaca;
}
