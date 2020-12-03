package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PovijestPrijavaStatisticsDto {

    private LocalDate datum;
    private Long broj;
}
