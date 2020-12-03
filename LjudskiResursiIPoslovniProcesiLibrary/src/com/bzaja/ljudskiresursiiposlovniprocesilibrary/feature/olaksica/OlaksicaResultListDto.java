package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OlaksicaResultListDto {

    private List<OlaksicaDetailsDto> olaksiceDetailsDto;
    private Double ukupanIznos;
}
