package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrekovremeniRadResultListDto {

    private List<PrekovremeniRadDetailsDto> prekovremeniRadoviDetailsDto;
    private Double ukupanIznos;
}
