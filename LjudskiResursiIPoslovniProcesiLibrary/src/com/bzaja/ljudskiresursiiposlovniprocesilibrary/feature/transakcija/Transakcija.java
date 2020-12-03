package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcije;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartner;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije.VrstaTransakcije;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Transakcija")
@Data
public class Transakcija implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDTransakcija")
    private Integer idTransakcija;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PoslovniPartnerID")
    private PoslovniPartner poslovniPartner;
    
    @Column(name = "Opis")
    private String opis;
    
    @Column(name = "Iznos")
    private Double iznos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;

    @Column(name = "SrednjiTecaj")
    private Double srednjiTecaj;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaTransakcijeID")
    private VrstaTransakcije vrstaTransakcije;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KategorijaTransakcijeID")
    private KategorijaTransakcije kategorijaTransakcije;

    @Column(name = "DatumTransakcije")
    private LocalDate datumTransakcije;
}
