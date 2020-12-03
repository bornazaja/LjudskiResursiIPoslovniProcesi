package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezDto;
import lombok.Data;

@Data
public class ObracunUgovoraPorezDto {

    private Integer idObracunUgovoraPorez;
    private ObracunUgovoraDto obracunUgovora;
    private PorezDto porez;
    private Double stopa;
    private Double minOsnovica;
    private Double maxOsnovica;
}
