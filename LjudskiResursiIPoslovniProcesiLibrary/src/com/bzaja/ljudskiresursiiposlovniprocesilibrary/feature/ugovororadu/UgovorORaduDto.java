package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UgovorORaduDto extends UgovorDto {

    @NotNull(message = "Broj radnih sati tjedno je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Broj radnih sati tjedno mora biti veća od 0")
    private Double brojRadnihSatiTjedno;

    @NotNull(message = "Bruto plaća je obavezna")
    @DecimalMin(value = "0", inclusive = false, message = "Bruto plaća mora biti veća od 0")
    private Double brutoPlaca;

    private ValutaDto valuta;
}
