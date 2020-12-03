package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class VrstaObustaveDto {

    private Integer idVrstaObustave;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
}
