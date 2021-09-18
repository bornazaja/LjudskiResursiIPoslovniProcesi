package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola.ZaposlenikRola;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Rola")
@Data
public class Rola implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRola")
    private Integer idRola;

    @Column(name = "Naziv")
    private String naziv;

    @ManyToMany(mappedBy = "rola", fetch = FetchType.LAZY)
    private Set<ZaposlenikRola> zaposleniciRole;
}
