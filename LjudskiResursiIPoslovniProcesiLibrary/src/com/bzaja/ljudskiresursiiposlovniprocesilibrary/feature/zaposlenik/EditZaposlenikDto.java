package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol.SpolDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.BrojTelefona;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.Dob;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.OIB;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.UniqueEmail;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.UniqueOIB;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@UniqueOIB(idField = "idZaposlenik", oibField = "oib", message = "OIB već postoji")
@UniqueEmail(idField = "idZaposlenik", emailField = "email", message = "Email već postoji")
public class EditZaposlenikDto {

    private Integer idZaposlenik;

    @NotEmpty(message = "Ime je obavezno")
    private String ime;

    @NotEmpty(message = "Prezime je obavezno")
    private String prezime;

    @NotNull(message = "Spol je obavezan")
    private SpolDto spol;

    @NotNull(message = "Datum rodjenja je obavezan")
    @Dob(min = 18, max = 100, message = "Dob mora biti izmeeđu 18 i 100 godina")
    private LocalDate datumRodjenja;

    @NotEmpty(message = "OIB je obavezan")
    @OIB(message = "OIB mora imati točno 11 znamenki")
    private String oib;

    @NotEmpty(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    private String email;

    @NotEmpty(message = "Broj telefona je obavezan")
    @BrojTelefona(message = "Broj telefona mora imati između 8 i 10 znamenki")
    private String brojTelefona;
}
