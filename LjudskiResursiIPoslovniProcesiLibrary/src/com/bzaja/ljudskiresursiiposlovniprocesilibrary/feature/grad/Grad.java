package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.Drzava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.Prebivaliste;
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
import lombok.ToString;

@Entity
@Table(name = "Grad")
@Data
@EqualsAndHashCode(exclude = "prebivalista")
public class Grad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDGrad")
    private Integer idGrad;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "Prirez")
    private Double prirez;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DrzavaID")
    private Drzava drzava;
    
    @OneToMany(mappedBy = "grad", fetch = FetchType.LAZY)
    private Set<Prebivaliste> prebivalista;
}
