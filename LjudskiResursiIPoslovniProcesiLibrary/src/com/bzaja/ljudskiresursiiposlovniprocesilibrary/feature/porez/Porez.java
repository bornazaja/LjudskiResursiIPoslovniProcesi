package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.Drzava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza.VrstaPoreza;
import java.io.Serializable;
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
@Table(name = "Porez")
@Data
public class Porez implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPorez")
    private Integer idPorez;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaPorezaID")
    private VrstaPoreza vrstaPoreza;
    
    @Column(name = "Stopa")
    private Double stopa;

    @Column(name = "MinOsnovica")
    private Double minOsnovica;
    
    @Column(name = "MaxOsnovica")
    private Double maxOsnovica;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DrzavaID")
    private Drzava drzava;
}
