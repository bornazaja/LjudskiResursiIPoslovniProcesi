package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.Obustava;
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
@Table(name = "VrstaObustave")
@Data
@EqualsAndHashCode(exclude = "obustave")
public class VrstaObustave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaObustave")
    private Integer idVrstaObustave;

    @Column(name = "Naziv")
    private String naziv;

    @OneToMany(mappedBy = "vrstaObustave", fetch = FetchType.LAZY)
    private Set<Obustava> obustave;
}
