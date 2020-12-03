package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ObracunUgovoraORaduDto extends ObracunUgovoraDto {

    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;

    @NotNull(message = "Datum do je obavezan")
    private LocalDate datumDo;

    private Double osnovniOsobniOdbitak;

    private Double osnovicaOsobnogOdbitka;
}
