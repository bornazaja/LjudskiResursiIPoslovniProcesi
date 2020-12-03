package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.Transakcija;
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
@Table(name = "VrstaTransakcije")
@Data
@EqualsAndHashCode(exclude = "transakcije")
public class VrstaTransakcije implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVrstaTransakcije")
    private Integer idVrstaTransakcije;

    @Column(name = "Naziv")
    private String naziv;

    @OneToMany(mappedBy = "vrstaTransakcije", fetch = FetchType.LAZY)
    private Set<Transakcija> transakcije;
}
