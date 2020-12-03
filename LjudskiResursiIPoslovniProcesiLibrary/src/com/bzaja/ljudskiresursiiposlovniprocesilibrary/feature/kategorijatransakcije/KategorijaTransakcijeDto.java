package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class KategorijaTransakcijeDto {

    private Integer idKategorijaTransakcije;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
}
