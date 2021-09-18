package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.Davanje;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
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
@Table(name = "IsplatnaListaDavanje")
@Data
public class IsplatnaListaDavanje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIsplatnaListaDavanje")
    private Integer idIsplatnaListaDavanje;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IsplatnaListaID")
    private IsplatnaLista isplatnaLista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DavanjeID")
    private Davanje davanje;

    @Column(name = "StopaNaPlacu")
    private Double stopaNaPlacu;

    @Column(name = "StopaIzPlace")
    private Double stopaIzPlace;
}
