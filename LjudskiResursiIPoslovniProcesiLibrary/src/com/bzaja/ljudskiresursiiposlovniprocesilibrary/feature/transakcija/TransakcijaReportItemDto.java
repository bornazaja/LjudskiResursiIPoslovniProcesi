package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije.VrstaTransakcijeDto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TransakcijaReportItemDto {

    private PoslovniPartnerDto poslovniPartner;
    private String opis;
    private Double iznosUOrginalnojValuti;
    private ValutaDto valuta;
    private Double iznosUDomacojValuti;
    private VrstaTransakcijeDto vrstaTransakcije;
    private KategorijaTransakcijeDto kategorijaTransakcija;
    private LocalDate datumTransakcije;
}
