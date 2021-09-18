package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora.VrstaUgovoraDto;
import lombok.Data;

@Data
public class RadniOdnosDto {

    private Integer idRadniOdnos;
    private String naziv;
    private VrstaUgovoraDto vrstaUgovora;
}
