package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije.VrstaTransakcijeDto;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class TransakcijaDto {

    private Integer idTransakcija;

    @NotNull(message = "Poslovni partner je obavezan")
    private PoslovniPartnerDto poslovniPartner;

    @NotEmpty(message = "Opis je obavezan")
    private String opis;

    @NotNull(message = "Iznos je obavezan")
    @DecimalMin(value = "0", inclusive = false, message = "Iznos mora biti veći od 0")
    private Double iznos;

    @NotNull(message = "Valuta je obavezna")
    private ValutaDto valuta;

    private Double srednjiTecaj;

    @NotNull(message = "Vrsta transakcije je obavezna")
    private VrstaTransakcijeDto vrstaTransakcije;

    @NotNull(message = "Kategorija transakcije je obavezna")
    private KategorijaTransakcijeDto kategorijaTransakcije;

    @NotNull(message = "Datum transakcije je obavezan")
    private LocalDate datumTransakcije;
}
