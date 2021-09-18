package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRad;
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
@Table(name = "VrstaPrekovremenogRada")
@Data
@EqualsAndHashCode(exclude = "prekovremeniRadovi")
public class VrstaPrekovremenogRada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaPrekovremenogRada")
    private Integer idVrstaPrekovremenogRada;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "Koeficjent")
    private Double koeficjent;

    @OneToMany(mappedBy = "vrstaPrekovremenogRada", fetch = FetchType.LAZY)
    private Set<PrekovremeniRad> prekovremeniRadovi;
}
