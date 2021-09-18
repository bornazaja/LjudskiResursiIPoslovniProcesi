package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DavanjeDto {

    private Integer idDavanje;

    @NotNull(message = "Vrsta davanja je obavezna")
    private VrstaDavanjaDto vrstaDavanja;
    
    private ZaposlenikDetailsDto zaposlenik;

    @NotNull(message = "Datum od je obavezan")
    private LocalDate datumOd;
    
    private LocalDate datumDo;
}
