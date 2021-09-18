package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import lombok.Data;

@Data
public class IsplatnaListaDodatakDto {

    private Integer idIsplatnaListaDodatak;
    private IsplatnaListaDto isplatnaLista;
    private DodatakDto dodatak;
}
