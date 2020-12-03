package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
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
@Table(name = "ParametriZaObracunPlace")
@Data
public class ParametriZaObracunPlace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDParametriZaObracunPlace")
    private Integer idParametriZaObracunPlace;
    
    @Column(name = "OsnovniOsobniOdbitak")
    private Double osnovniOsobniOdbitak;
    
    @Column(name = "OsnovicaOsobnogOdbitka")
    private Double osnovicaOsobnogOdbitka;
    
    @Column(name = "LimitGodisnjegIznosaZaStudenta")
    private Double limitGodisnjegIznosaZaStudenta;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;
}
