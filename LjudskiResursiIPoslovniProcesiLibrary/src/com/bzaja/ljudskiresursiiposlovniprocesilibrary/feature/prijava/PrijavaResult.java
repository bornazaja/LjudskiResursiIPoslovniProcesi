package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrijavaResult {

    private Boolean jePrijavaUspjela;
    private PrijavljeniZaposlenikDto prijavljeniZaposlenikDto;
}
