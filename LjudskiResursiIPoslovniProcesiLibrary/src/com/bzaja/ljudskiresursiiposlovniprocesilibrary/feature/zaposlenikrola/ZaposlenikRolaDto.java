package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ZaposlenikRolaDto {

    private Integer idZaposlenikRola;

    private ZaposlenikDetailsDto zaposlenik;

    @NotNull(message = "Rola je obavezna")
    private RolaDto rola;
}
