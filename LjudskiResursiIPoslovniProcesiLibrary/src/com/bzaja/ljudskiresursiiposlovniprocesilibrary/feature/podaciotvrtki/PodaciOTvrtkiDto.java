package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.BrojTelefona;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.OIB;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PodaciOTvrtkiDto {
    
    private Integer idPodaciOTvrtki;
    
    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;
    
    @NotEmpty(message = "Ulica je obavezna")
    private String ulica;
    
    @NotNull(message = "Grad je obavezan")
    private GradDto grad;
    
    @NotEmpty(message = "OIB je obavezan")
    @OIB(message = "OIB mora imati točno 11 znamenki")
    private String oib;
    
    @NotEmpty(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    private String email;
    
    @NotEmpty(message = "Broj telefona je obavezan")
    @BrojTelefona(message = "Broj telefona mora imati izmedju 8 i 10 znakova")
    private String brojTelefona;
}
