package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.Grad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.Porez;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Drzava")
@Data
@EqualsAndHashCode(exclude = {"gradovi", "porezniRazredi"})
public class Drzava implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDrzava")
    private Integer idDrzava;

    @Column(name = "Naziv")
    private String naziv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;
    
    @Column(name = "JeDomovina")
    private Boolean jeDomovina;
    
    @OneToMany(mappedBy = "drzava", fetch = FetchType.LAZY)
    private Set<Grad> gradovi;
    
    @OneToMany(mappedBy = "drzava", fetch = FetchType.LAZY)
    private Set<Porez> porezniRazredi;
}
