package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.Porez;
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
@Table(name = "ObracunUgovoraPorez")
@Data
public class ObracunUgovoraPorez implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDObracunUgovoraPorez")
    private Integer idObracunUgovoraPorezniRazred;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ObracunUgovoraID")
    private ObracunUgovora obracunUgovora;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PorezID")
    private Porez porez;

    @Column(name = "Stopa")
    private Double stopa;

    @Column(name = "MinOsnovica")
    private Double minOsnovica;

    @Column(name = "MaxOsnovica")
    private Double maxOsnovica;
}
