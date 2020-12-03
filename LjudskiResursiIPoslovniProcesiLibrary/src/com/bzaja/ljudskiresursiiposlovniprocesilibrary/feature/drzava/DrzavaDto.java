package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class DrzavaDto {

    private Integer idDrzava;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    private ValutaDto valuta;
    
    private Boolean jeDomovina;
}
