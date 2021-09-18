package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.Lozinka;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.ProvjeraStareLozinke;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ProvjeraStareLozinke(message = "Stara lozinka nije točna")
public class ChangePasswordZaposlenikDto {

    private Integer idZaposlenik;

    @NotEmpty(message = "Stara lozinka je obavezna")
    private String staraLozinka;

    @NotEmpty(message = "Nova lozinka je obavezna")
    @Lozinka(message = "Nova lozinka nije ispravna, mora sadržavati sljedeće: minimum 6 znakova, barem jedno veliko slovo, barem jedno malo slovo, barem jedan broj, barem jedan specijalni znak")
    private String novaLozinka;

    @NotEmpty(message = "Ponovi novu lozinku je obavezno")
    private String ponoviNovuLozinku;
}
