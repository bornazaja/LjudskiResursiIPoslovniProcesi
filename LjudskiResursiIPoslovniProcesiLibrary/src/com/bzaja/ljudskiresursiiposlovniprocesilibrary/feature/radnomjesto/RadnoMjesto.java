package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.Odjel;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "RadnoMjesto")
@Data
@EqualsAndHashCode(exclude = "ugovori")
public class RadnoMjesto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRadnoMjesto")
    private Integer idRadnoMjesto;

    @Column(name = "Naziv")
    private String naziv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OdjelID")
    private Odjel odjel;

    @OneToMany(mappedBy = "radnoMjesto", fetch = FetchType.LAZY)
    private Set<Ugovor> ugovori;
}
