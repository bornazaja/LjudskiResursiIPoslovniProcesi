package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez.ObracunUgovoraPorez;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrstaObracuna;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ObracunUgovora")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(exclude = {"isplatneListe", "obracuniUgovoraPorezi"})
public class ObracunUgovora implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDObracunUgovora")
    private Integer idObracunUgovora;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaObracunaID")
    private VrstaObracuna vrstaObracuna;
    
    @Column(name = "Opis")
    private String opis;
    
    @Column(name = "DatumObracuna")
    private LocalDate datumObracuna;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;

    @ManyToMany(mappedBy = "obracunUgovora", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<IsplatnaLista> isplatneListe;

    @ManyToMany(mappedBy = "obracunUgovora", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<ObracunUgovoraPorez> obracuniUgovoraPorezi;
}
