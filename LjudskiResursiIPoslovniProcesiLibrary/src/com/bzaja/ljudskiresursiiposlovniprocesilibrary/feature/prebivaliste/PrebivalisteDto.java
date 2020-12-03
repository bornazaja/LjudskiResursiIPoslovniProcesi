package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PrebivalisteDto {

    private Integer idPrebivaliste;
    
    @NotEmpty(message = "Ulica je obavezna")
    private String ulica;
    
    @NotNull(message = "Grad je obavezan")
    private GradDto grad;
    
    private ZaposlenikDetailsDto zaposlenik;
    
    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;
}
