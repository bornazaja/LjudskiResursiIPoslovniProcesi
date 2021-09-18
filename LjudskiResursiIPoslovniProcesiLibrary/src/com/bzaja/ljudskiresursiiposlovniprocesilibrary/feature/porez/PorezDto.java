package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza.VrstaPorezaDto;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PorezDto {

    private Integer idPorez;
    
    @NotNull(message = "Stopa je obavezna")
    @DecimalMin(value = "0", inclusive = false, message = "Stopa mora biti veći od 0")
    private Double stopa;
    
    @NotNull(message = "Vrsta poreza je obavezna")
    private VrstaPorezaDto vrstaPoreza;
    
    @NotNull(message = "Minimalna osnovica je obavezna")
    @DecimalMin(value = "0", inclusive = true, message = "Minimalna osnovica mora biti veća ili jednaka 0")
    private Double minOsnovica;
    
    @DecimalMin(value = "0", inclusive = true, message = "Maximalna osnovica mora biti veća ili jednaka 0")
    private Double maxOsnovica;
    
    @NotNull(message = "Drzava je obavezna")
    private DrzavaDto drzava;
}
