package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "UgovorODjelu")
@PrimaryKeyJoinColumn(name = "IDUgovorODjelu")
@Data
public class UgovorODjelu extends Ugovor implements Serializable {

    @Column(name = "BrutoIznos")
    private Double brutoIznos;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;
    
    @Column(name = "StopaPausalnogPriznatogTroska")
    private Double stopaPausalnogPriznatogTroska;
    
    @Column(name = "JeObracunat")
    private Boolean jeObracunat;
}
