package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.Porez;
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
@Table(name = "VrstaPoreza")
@Data
@EqualsAndHashCode(exclude = {"porezi"})
public class VrstaPoreza implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaPoreza")
    private Integer idVrstaPoreza;

    @Column(name = "Naziv")
    private String naziv;

    @OneToMany(mappedBy = "vrstaPoreza", fetch = FetchType.LAZY)
    private Set<Porez> porezi;
}
