package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaDto;
import lombok.Data;

@Data
public class PrijavljeniZaposlenikDto {

    private ZaposlenikDetailsDto zaposlenik;
    private RolaDto trenutnaRola;
}
