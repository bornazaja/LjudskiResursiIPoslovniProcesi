package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OlaksicaDto {

    private Integer idOlaksica;

    @NotNull(message = "Vrsta olaksice je obavezna")
    private VrstaOlaksiceDto vrstaOlaksice;
    
    private ZaposlenikDetailsDto zaposlenik;
    
    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;
    
    @NotNull(message = "Datum do je obavezan")
    private LocalDate datumDo;
}
