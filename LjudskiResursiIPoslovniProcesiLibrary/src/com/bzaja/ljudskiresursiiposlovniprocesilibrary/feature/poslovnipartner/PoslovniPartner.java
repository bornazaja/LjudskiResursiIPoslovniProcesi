package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.Grad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.Transakcija;
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
@Table(name = "PoslovniPartner")
@Data
@EqualsAndHashCode(exclude = "transakcije")
public class PoslovniPartner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPoslovniPartner")
    private Integer idPoslovniPartner;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "OIB")
    private String oib;

    @Column(name = "Email")
    private String email;

    @Column(name = "BrojTelefona")
    private String brojTelefona;

    @Column(name = "Ulica")
    private String ulica;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GradID")
    private Grad grad;
    
    @OneToMany(mappedBy = "poslovniPartner", fetch = FetchType.LAZY)
    private Set<Transakcija> transakcije;
}
