package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorDto;
import lombok.Data;

@Data
public class IsplatnaListaDto {

    private Integer idIsplatnaLista;
    private ObracunUgovoraDto obracunUgovora;
    private UgovorDto ugovor;
    private PrebivalisteDto prebivaliste;
    private Double prirez;
}
