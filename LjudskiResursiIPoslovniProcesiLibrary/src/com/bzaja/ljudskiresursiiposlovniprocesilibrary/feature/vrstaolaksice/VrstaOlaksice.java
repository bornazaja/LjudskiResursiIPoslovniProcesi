package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.Olaksica;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "VrstaOlaksice")
@Data
@EqualsAndHashCode(exclude = "olaksice")
public class VrstaOlaksice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaOlaksice")
    private Integer idVrstaOlaksice;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "Koeficjent")
    private Double koeficjent;

    @Column(name = "VrijediDo")
    private LocalDate vrijediDo;

    @OneToMany(mappedBy = "vrstaOlaksice", fetch = FetchType.LAZY)
    private Set<Olaksica> olaksice;
}
