package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica.IsplatnaListaOlaksica;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksice;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.Zaposlenik;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Olaksica")
@Data
@EqualsAndHashCode(exclude = "isplatneListeOlaksice")
public class Olaksica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDOlaksica")
    private Integer idOlaksica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaOlaksiceID")
    private VrstaOlaksice vrstaOlaksice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;

    @Column(name = "DatumOd")
    private LocalDate datumOd;

    @Column(name = "DatumDo")
    private LocalDate datumDo;

    @ManyToMany(mappedBy = "olaksica", fetch = FetchType.LAZY)
    private Set<IsplatnaListaOlaksica> isplatneListeOlaksice;
}
