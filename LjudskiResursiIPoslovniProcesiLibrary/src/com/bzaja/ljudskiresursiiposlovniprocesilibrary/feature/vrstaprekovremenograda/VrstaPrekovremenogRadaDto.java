package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class VrstaPrekovremenogRadaDto {

    private Integer idVrstaPrekovremenogRada;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    @NotNull(message = "Koeficjent je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Koeficjent mora biti veći od 0")
    private Double koeficjent;
}
