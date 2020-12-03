package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaStudentskogUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import java.util.List;
import lombok.Data;

@Data
public class ObracunStudentskihUgovoraResultDto extends ObracunStudentskihUgovoraDto {

    private PodaciOTvrtkiDto podaciOTvrtki;
    private List<IsplatnaListaStudentskogUgovoraDto> isplatneListeStudentskihUgovora;
}
