package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja;

import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class VrstaDavanjaDto {

    private Integer idVrstaDavanja;

    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;

    @NotNull(message = "Stopa na plaću je obavezna")
    @DecimalMin(value = "0", inclusive = true, message = "Stopa na placu mora biti veća ili jednaka 0")
    private Double stopaNaPlacu;

    @NotNull(message = "Stopa iz plaće je obavezna")
    @DecimalMin(value = "0", inclusive = true, message = "Stopa iz place mora biti veća ili jednaka 0")
    private Double stopaIzPlace;

    private LocalDate vrijediDo;
}
