package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaDto;
import lombok.Data;

@Data
public class IsplatnaListaObustavaDto {

    private Integer idIsplatnaListaObustava;
    private IsplatnaListaDto isplatnaLista;
    private ObustavaDto obustava;
}
