package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParametriZaObracunPlaceDto {

    private Integer idParametriZaObracunPlace;

    @NotNull(message = "Osnovni osobni odbitak je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Osnovni osobni odbitak mora biti veći od 0")
    private Double osnovniOsobniOdbitak;

    @NotNull(message = "Osnovica osobnog odbitka je obavezna")
    @DecimalMin(value = "0", inclusive = false, message = "Osnovica osobnog odbitka mora biti veća od 0")
    private Double osnovicaOsobnogOdbitka;

    @NotNull(message = "Limit godišnjeg iznosa za studenta je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Limit godišnjeg iznos za studenta mora biti veći do 0")
    private Double limitGodisnjegIznosaZaStudenta;

    private ValutaDto valuta;
}
