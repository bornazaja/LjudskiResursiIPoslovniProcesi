package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol.SpolDto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ZaposlenikDetailsDto {

    private Integer idZaposlenik;
    private String ime;
    private String prezime;
    private SpolDto spol;
    private LocalDate datumRodjenja;
    private String oib;
    private String email;
    private String brojTelefona;
}
