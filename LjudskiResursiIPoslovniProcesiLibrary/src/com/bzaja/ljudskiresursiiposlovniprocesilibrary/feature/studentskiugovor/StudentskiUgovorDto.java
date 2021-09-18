package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint.MinimalnaCijenaPoSatuStudentskogUgovora;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@MinimalnaCijenaPoSatuStudentskogUgovora(message = "Cijena po satu mora biti veća ili jednaka cijeni po satu koja je definiranu u cjeniku studentskih poslova")
public class StudentskiUgovorDto extends UgovorDto {

    @NotNull(message = "Studentski posao cjenik je obavezan")
    private StudentskiPosaoCjenikDto studentskiPosaoCjenik;

    @NotNull(message = "Broj odrađenih sati je obavezan")
    @Min(value = 1, message = "Broj odrađenih sati mora biti veći ili jednak jedan")
    private Double brojOdradjenihSati;

    @NotNull(message = "Cijena po satu je obavezna")
    @DecimalMin(value = "0", inclusive = false, message = "Cijena po satu mora biti veća od 0")
    private Double cijenaPoSatu;

    @NotNull(message = "Dosad zarađeni iznos u ovoj godini je obavezan")
    @DecimalMin(value = "0", inclusive = true, message = "Dosad zarađeni iznos u ovoj godini mora biti veći ili jednak 0")
    private Double dosadZaradjeniIznosUOvojGodini;

    private ValutaDto valuta;

    private Boolean jeObracunat;
}
