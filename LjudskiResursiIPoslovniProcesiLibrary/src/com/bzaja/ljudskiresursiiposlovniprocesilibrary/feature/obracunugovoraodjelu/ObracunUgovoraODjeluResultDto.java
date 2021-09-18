package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import java.util.List;
import lombok.Data;

@Data
public class ObracunUgovoraODjeluResultDto extends ObracunUgovoraDto {

    private PodaciOTvrtkiDto podaciOTvrtki;
    private List<IsplatnaListaUgovoraODjeluDto> isplatneListeUgovoraODjelu;
}
