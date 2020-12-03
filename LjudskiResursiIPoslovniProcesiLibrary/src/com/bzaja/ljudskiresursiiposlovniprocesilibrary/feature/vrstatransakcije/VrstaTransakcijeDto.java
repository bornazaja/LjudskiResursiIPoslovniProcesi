package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class VrstaTransakcijeDto {

    private Integer idVrstaTransakcije;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
}
