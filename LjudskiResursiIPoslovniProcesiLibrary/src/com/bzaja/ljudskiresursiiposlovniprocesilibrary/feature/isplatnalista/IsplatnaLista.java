package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje.IsplatnaListaDavanje;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak.IsplatnaListaDodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava.IsplatnaListaObustava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica.IsplatnaListaOlaksica;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad.IsplatnaListaPrekovremeniRad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.Prebivaliste;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "IsplatnaLista")
@Data
@EqualsAndHashCode(exclude = {"isplatneListeDavanja", "isplatneListeOlaksice", "isplatneListeDodatci", "isplatneListeObustave", "isplatneListePrekovremeniRadovi"})
public class IsplatnaLista implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIsplatnaLista")
    private Integer idIsplatnaLista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ObracunUgovoraID")
    private ObracunUgovora obracunUgovora;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UgovorID")
    private Ugovor ugovor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PrebivalisteID")
    private Prebivaliste prebivaliste;

    @Column(name = "Prirez")
    private Double prirez;

    @ManyToMany(mappedBy = "isplatnaLista", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<IsplatnaListaDavanje> isplatneListeDavanja;

    @ManyToMany(mappedBy = "isplatnaLista", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<IsplatnaListaDodatak> isplatneListeDodatci;

    @ManyToMany(mappedBy = "isplatnaLista", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<IsplatnaListaObustava> isplatneListeObustave;

    @ManyToMany(mappedBy = "isplatnaLista", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<IsplatnaListaOlaksica> isplatneListeOlaksice;

    @ManyToMany(mappedBy = "isplatnaLista", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<IsplatnaListaPrekovremeniRad> isplatneListePrekovremeniRadovi;
}
