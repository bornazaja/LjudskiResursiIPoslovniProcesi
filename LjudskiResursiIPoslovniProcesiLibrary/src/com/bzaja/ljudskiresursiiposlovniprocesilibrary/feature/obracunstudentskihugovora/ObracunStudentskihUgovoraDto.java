package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import lombok.Data;

@Data
public class ObracunStudentskihUgovoraDto extends ObracunUgovoraDto {

    private Double limitGodisnjegIznosaZaStudenta;
}
