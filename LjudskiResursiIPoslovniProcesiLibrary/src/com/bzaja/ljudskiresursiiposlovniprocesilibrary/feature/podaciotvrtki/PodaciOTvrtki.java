package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.Grad;
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
@Table(name = "PodaciOTvrtki")
@Data
public class PodaciOTvrtki implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPodaciOTvrtki")
    private Integer idPodaciOTvrtki;
    
    @Column(name = "Naziv")
    private String naziv;
    
    @Column(name = "Ulica")
    private String ulica;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GradID")
    private Grad grad;
    
    @Column(name = "OIB")
    private String oib;
    
    @Column(name = "Email")
    private String email;
    
    @Column(name = "BrojTelefona")
    private String brojTelefona;
}
