package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PovijestPrijavaDto {

    private Integer idPovijestPrijava;
    private LocalDateTime vrijemePrijave;
    private ZaposlenikDetailsDto zaposlenik;
    private RolaDto rola;
}
