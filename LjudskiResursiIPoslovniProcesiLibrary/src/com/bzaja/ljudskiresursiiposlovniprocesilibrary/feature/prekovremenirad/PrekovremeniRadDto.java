package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PrekovremeniRadDto {

    private Integer idPrekovremeniRad;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    @NotNull(message = "Vrsta prekovremenog rada je obavezna")
    private VrstaPrekovremenogRadaDto vrstaPrekovremenogRada;
    
    @NotNull(message = "Broj dodatnih sati je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Broj dotatnih sati mora biti veći od 0")
    private Double brojDodatnihSati;
    
    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;
    
    @NotNull(message = "Datum do je obavezan")
    private LocalDate datumDo;
    
    private ZaposlenikDetailsDto zaposlenik;
}
