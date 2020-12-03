package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class VrstaDodatkaDto {

    private Integer idVrstaDodatka;

    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
}
