package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos.RadniOdnos;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "VrstaUgovora")
@Data
@EqualsAndHashCode(exclude = "radniOdnosi")
public class VrstaUgovora implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaUgovora")
    private Integer idVrstaUgovora;

    @Column(name = "Naziv")
    private String naziv;
    
    @OneToMany(mappedBy = "vrstaUgovora", fetch = FetchType.LAZY)
    private Set<RadniOdnos> radniOdnosi;
}
