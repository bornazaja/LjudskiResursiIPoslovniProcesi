package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaDto;
import lombok.Data;

@Data
public class IsplatnaListaOlaksicaDto {

    private Integer idIsplatnaListaOlaksica;
    private IsplatnaListaDto isplatnaLista;
    private OlaksicaDto olaksica;
    private Double koeficjent;
}
