package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UgovorODjeluDto extends UgovorDto {

    @NotNull(message = "Bruto iznos je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Bruto iznos mora biti veći od 0")
    private Double brutoIznos;

    private ValutaDto valuta;

    @NotNull(message = "Stopa paušalnog priznatog troška je obavezna")
    @DecimalMin(value = "0", inclusive = true, message = "Stopa paušalnog priznatog troška mora biti veći ili jednak 0")
    private Double stopaPausalnogPriznatogTroska;

    private Boolean jeObracunat;
}
