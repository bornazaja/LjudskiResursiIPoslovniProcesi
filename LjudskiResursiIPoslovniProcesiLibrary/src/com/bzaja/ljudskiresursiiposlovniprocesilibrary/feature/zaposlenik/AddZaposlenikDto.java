package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol.SpolDto;
import com.bzaja.myjavalibrary.validation.customconstraint.PhoneNumber;
import com.bzaja.myjavalibrary.validation.customconstraint.Age;
import com.bzaja.myjavalibrary.validation.customconstraint.FieldMatch;
import com.bzaja.myjavalibrary.validation.customconstraint.StrongPassword;
import com.bzaja.myjavalibrary.validation.customconstraint.OIB;
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
@FieldMatch(first = "lozinka", second = "ponoviLozinku", message = "Lozinke se ne podudaraju")
public class AddZaposlenikDto {

    private Integer idZaposlenik;

    @NotEmpty(message = "Ime je obavezno")
    private String ime;

    @NotEmpty(message = "Prezime je obavezno")
    private String prezime;

    @NotNull(message = "Spol je obavezan")
    private SpolDto spol;

    @NotNull(message = "Datum rodjenja je obavezan")
    @Age(min = 18, max = 100, message = "Dob mora biti između 18 i 100 godina")
    private LocalDate datumRodjenja;

    @NotEmpty(message = "OIB je obavezan")
    @OIB(message = "OIB mora imati točno 11 znamenki")
    private String oib;

    @NotEmpty(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    private String email;

    @NotEmpty(message = "Lozinka je obavezna")
    @StrongPassword(message = "Lozinka nije ispravna, mora sadržavati sljedeće: minimum 8 znakova, barem jedno veliko slovo, barem jedno malo slovo, barem jedan broj, barem jedan specijalni znak")
    private String lozinka;

    @NotEmpty(message = "Ponovi lozinku je obavezno")
    private String ponoviLozinku;

    @NotEmpty(message = "Broj telefona je obavezan")
    @PhoneNumber(message = "Broj telefona mora imati između 8 i 10 znamenki")
    private String brojTelefona;
}
