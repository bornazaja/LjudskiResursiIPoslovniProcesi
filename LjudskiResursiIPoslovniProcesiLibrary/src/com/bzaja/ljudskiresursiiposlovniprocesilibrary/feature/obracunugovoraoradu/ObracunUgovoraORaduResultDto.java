package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import java.util.List;
import lombok.Data;

@Data
public class ObracunUgovoraORaduResultDto extends ObracunUgovoraORaduDto {

    private PodaciOTvrtkiDto podaciOTvrtki;
    private List<IsplatnaListaUgovoraORaduDto> isplatneListeUgovoraORadu;
}
