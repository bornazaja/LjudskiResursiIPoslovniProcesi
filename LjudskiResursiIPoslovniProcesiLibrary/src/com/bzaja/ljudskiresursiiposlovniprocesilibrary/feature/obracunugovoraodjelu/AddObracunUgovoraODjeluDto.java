package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.NotEmptyList;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.UniqueElements;
import java.util.List;
import lombok.Data;

@Data
public class AddObracunUgovoraODjeluDto extends ObracunUgovoraDto {

    @NotEmptyList(message = "Idevi ugovora su obavezni")
    @UniqueElements(message = "Idevi ugovora moraju biti jedinstveni")
    private List<Integer> ideviUgovora;
}
