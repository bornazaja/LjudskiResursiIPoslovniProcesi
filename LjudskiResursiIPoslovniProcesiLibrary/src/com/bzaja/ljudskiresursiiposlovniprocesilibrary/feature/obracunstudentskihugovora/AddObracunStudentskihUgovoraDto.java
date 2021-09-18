package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.NotEmptyList;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.UniqueElements;
import java.util.List;
import lombok.Data;

@Data
public class AddObracunStudentskihUgovoraDto extends ObracunUgovoraDto {

    @NotEmptyList(message = "Idevi ugovora su obavezni")
    @UniqueElements(message = "Idevi ugovora moraju biti jedinstveni")
    private List<Integer> ideviUgovora;

    private Double limitGodisnjegIznosaZaStudenta;
}
