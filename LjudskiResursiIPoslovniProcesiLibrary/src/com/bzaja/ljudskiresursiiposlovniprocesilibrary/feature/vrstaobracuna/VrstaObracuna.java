package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VrstaObracuna")
@Data
public class VrstaObracuna implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaObracuna")
    private Integer idVrstaObracuna;
    
    @Column(name = "Naziv")
    private String naziv;
}
