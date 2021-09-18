package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.BrojTelefona;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.OIB;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.UniqueEmail;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.UniqueOIB;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@UniqueOIB(idField = "idPoslovniPartner", oibField = "oib", message = "OIB već postoji")
@UniqueEmail(idField = "idPoslovniPartner", emailField = "email", message = "Email već postoji")
public class PoslovniPartnerDto {

    private Integer idPoslovniPartner;

    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;

    @OIB(message = "OIB mora imati toćno 11 znamenki", ignoreEmptyString = true)
    private String oib;

    @NotEmpty(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    private String email;

    @NotEmpty(message = "Broj telefona je obavezan")
    @BrojTelefona(message = "Broj telefona mora biti između 8 i 10 znamenki")
    private String brojTelefona;

    @NotEmpty(message = "Ulica je obavezna")
    private String ulica;

    @NotNull(message = "Grad je obavezan")
    private GradDto grad;
}
