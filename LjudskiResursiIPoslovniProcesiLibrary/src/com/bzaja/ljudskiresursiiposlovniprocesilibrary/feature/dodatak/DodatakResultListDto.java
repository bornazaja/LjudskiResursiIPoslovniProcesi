package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DodatakResultListDto {

    private List<DodatakDetailsDto> dodatciDetailsDto;
    private Double ukupanIznos;
}
