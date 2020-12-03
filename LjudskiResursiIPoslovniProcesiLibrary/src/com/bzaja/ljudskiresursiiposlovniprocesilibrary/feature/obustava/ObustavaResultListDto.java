package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ObustavaResultListDto {

    private List<ObustavaDetailsDto> obustaveDetailsDto;
    private Double ukupanIznos;
}
