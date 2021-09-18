package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelDto;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class RadnoMjestoDto {

    private Integer idRadnoMjesto;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    @NotNull(message = "Odjel je obavezan")
    private OdjelDto odjel;
}
