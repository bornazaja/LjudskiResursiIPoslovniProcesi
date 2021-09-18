package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class StudentskiPosaoCjenikDto {

    private Integer idStudentskiPosaoCjenik;

    @NotEmpty(message = "Naziv je obavezan")
    private String naziv;

    @NotNull(message = "Cijena po satu je obavezna")
    @DecimalMin(value = "0", inclusive = false, message = "Cijena po satu mora biti veća od 0")
    private Double cijenaPoSatu;

    private ValutaDto valuta;
}
