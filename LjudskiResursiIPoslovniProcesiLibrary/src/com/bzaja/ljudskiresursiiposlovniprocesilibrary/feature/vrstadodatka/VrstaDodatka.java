package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.Dodatak;
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
@Table(name = "VrstaDodatka")
@Data
@EqualsAndHashCode(exclude = "dodatci")
public class VrstaDodatka implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaDodatka")
    private Integer idVrstaDodatka;

    @Column(name = "Naziv")
    private String naziv;

    @OneToMany(mappedBy = "vrstaDodatka", fetch = FetchType.LAZY)
    private Set<Dodatak> dodatci;
}
