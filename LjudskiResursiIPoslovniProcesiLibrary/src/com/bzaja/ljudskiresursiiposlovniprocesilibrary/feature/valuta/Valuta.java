package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.Dodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.Drzava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.Obustava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlace;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenik;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.Transakcija;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORadu;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Valuta")
@Data
@EqualsAndHashCode(exclude = {"transakcije", "studentskiPosloviCjenik", "parametriZaObracunPlace", "obracuniUgovora", "dodatci", "obustave", "ugovoriORadu", "studentskiUgovori", "drzave"})
public class Valuta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDValuta")
    private Integer idValuta;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "Jedinica")
    private Integer jedinica;

    @Column(name = "SrednjiTecaj")
    private Double srednjiTecaj;

    @Column(name = "DatumTecaja")
    private LocalDate datumTecaja;

    @Column(name = "JeAktivna")
    private Boolean jeAktivna;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<Dodatak> dodatci;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<Obustava> obustave;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<Transakcija> transakcije;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<UgovorORadu> ugovoriORadu;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<StudentskiUgovor> studentskiUgovori;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<StudentskiPosaoCjenik> studentskiPosloviCjenik;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<ParametriZaObracunPlace> parametriZaObracunPlace;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<Drzava> drzave;

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private Set<ObracunUgovora> obracuniUgovora;
}
