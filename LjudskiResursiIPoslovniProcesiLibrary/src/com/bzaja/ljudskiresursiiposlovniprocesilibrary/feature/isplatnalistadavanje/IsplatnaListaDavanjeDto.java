package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import lombok.Data;

@Data
public class IsplatnaListaDavanjeDto {

    private Integer idIsplatnaListaDavanje;
    private IsplatnaListaDto isplatnaLista;
    private DavanjeDto davanje;
    private Double stopaNaPlacu;
    private Double stopaIzPlace;
}
