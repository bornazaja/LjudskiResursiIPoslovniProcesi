package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadDto;
import lombok.Data;

@Data
public class IsplatnaListaPrekovremeniRadDto {

    private Integer idIsplatnaListaPrekovremeniRad;
    private IsplatnaListaDto isplatnaLista;
    private PrekovremeniRadDto prekovremeniRad;
    private Double koeficjent;
}
