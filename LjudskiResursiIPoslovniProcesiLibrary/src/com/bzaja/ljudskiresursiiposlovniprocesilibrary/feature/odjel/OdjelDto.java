package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class OdjelDto {

    private Integer idOdjel;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
}
