package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.Grad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.Zaposlenik;
import java.io.Serializable;
import java.time.LocalDate;
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
import lombok.ToString;

@Entity
@Table(name = "Prebivaliste")
@Data
public class Prebivaliste implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPrebivaliste")
    private Integer idPrebivaliste;
    
    @Column(name = "Ulica")
    private String ulica;
    
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GradID")
    private Grad grad;
    
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;
    
    @Column(name = "DatumOd")
    private LocalDate datumOd;
}
