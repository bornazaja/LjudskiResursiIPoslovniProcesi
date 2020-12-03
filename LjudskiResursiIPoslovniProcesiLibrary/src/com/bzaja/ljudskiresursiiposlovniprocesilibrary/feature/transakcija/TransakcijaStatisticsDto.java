package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TransakcijaStatisticsDto {

    private String vrstaTransakcije;
    private Long broj;
}
