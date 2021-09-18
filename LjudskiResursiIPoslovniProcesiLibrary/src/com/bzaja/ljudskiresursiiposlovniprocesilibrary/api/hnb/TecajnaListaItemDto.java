package com.bzaja.ljudskiresursiiposlovniprocesilibrary.api.hnb;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TecajnaListaItemDto {

    @SerializedName(value = "Broj tečajnice")
    private Integer brojTecajnice;
    
    @SerializedName(value = "Datum primjene")
    private LocalDate datumPrimjene;
    
    @SerializedName(value = "Država")
    private String drzava;
    
    @SerializedName(value = "Šifra valute")
    private String sifraValute;
    
    @SerializedName(value = "Valuta")
    private String valuta;
    
    @SerializedName(value = "Jedinica")
    private Integer jedinica;
    
    @SerializedName(value = "Kupovni za devize")
    private Double kupovniZaDevize;
    
    @SerializedName(value = "Srednji za devize")
    private Double srednjiZaDevize;
    
    @SerializedName(value = "Prodajni za devize")
    private Double prodajniZaDevize;
}
