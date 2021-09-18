package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos.RadniOdnosDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UgovorDto {

    private Integer idUgovor;
    
    @NotNull(message = "Radni odnos je obavezan")
    private RadniOdnosDto radniOdnos;
    
    @NotNull(message = "Zaposlenik je obavezan")
    private ZaposlenikDetailsDto zaposlenik;
    
    @NotNull(message = "Radno mjesto je obavezno")
    private RadnoMjestoDto radnoMjesto;
    
    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;
    
    private LocalDate datumDo;
}
