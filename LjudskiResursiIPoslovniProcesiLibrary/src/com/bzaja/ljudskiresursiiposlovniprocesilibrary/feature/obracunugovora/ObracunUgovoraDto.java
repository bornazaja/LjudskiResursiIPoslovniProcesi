package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrstaObracunaDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ObracunUgovoraDto {

    private Integer idObracunUgovora;

    private VrstaObracunaDto vrstaObracuna;

    @NotEmpty(message = "Opis je obavezan")
    private String opis;

    @NotNull(message = "Datum obračuna je obavezan")
    private LocalDate datumObracuna;

    private ValutaDto valuta;
}
