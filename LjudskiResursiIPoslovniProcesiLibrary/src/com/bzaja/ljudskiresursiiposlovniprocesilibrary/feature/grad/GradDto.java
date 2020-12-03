package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaDto;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class GradDto {

    private Integer idGrad;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    @Min(value = 0,  message = "Prirez mora biti veći ili jednak 0")
    private Double prirez;
    
    @NotNull(message = "Drzava je obavezna")
    private DrzavaDto drzava;
}
