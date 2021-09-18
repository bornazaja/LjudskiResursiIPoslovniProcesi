package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice;

import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class VrstaOlaksiceDto {

    private Integer idVrstaOlaksice;

    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;

    @NotNull(message = "Koeficjent je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Koeficjent mora biti veći od 0")
    private Double koeficjent;

    private LocalDate vrijediDo;
}
