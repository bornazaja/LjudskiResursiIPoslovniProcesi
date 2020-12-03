package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjesto;
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
@Table(name = "Odjel")
@Data
@EqualsAndHashCode(exclude = "radnaMjesta")
public class Odjel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDOdjel")
    private Integer idOdjel;

    @Column(name = "Naziv")
    private String naziv;

    @OneToMany(mappedBy = "odjel", fetch = FetchType.LAZY)
    private Set<RadnoMjesto> radnaMjesta;
}
