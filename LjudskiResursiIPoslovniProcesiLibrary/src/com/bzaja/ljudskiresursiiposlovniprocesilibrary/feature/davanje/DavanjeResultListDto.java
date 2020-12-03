package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DavanjeResultListDto {

    private List<DavanjeDetailsDto> davanjaDetailsDto;
    private Double ukupanIznosDavanjaNaPlacu;
    private Double ukupanIznosDavanjaIzPlace;
}
