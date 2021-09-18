package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatkaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class DodatakDto {

    private Integer idDodatak;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    @NotNull(message = "Vrsta dodatka je obavezan")
    private VrstaDodatkaDto vrstaDodatka;
    
    @NotNull(message = "Iznos je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Iznos mora biti veći od 0")
    private Double iznos;
    
    private ValutaDto valuta;
    
    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;
    
    @NotNull(message = "Datum do je obavezan")
    private LocalDate datumDo;
    
    private ZaposlenikDetailsDto zaposlenik;
}
