package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad.IsplatnaListaPrekovremeniRad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRada;
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
@Table(name = "PrekovremeniRad")
@Data
@EqualsAndHashCode(exclude = "isplatneListePrekovremeniRadovi")
public class PrekovremeniRad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPrekovremeniRad")
    private Integer idPrekovremeniRad;

    @Column(name = "Naziv")
    private String naziv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaPrekovremenogRadaID")
    private VrstaPrekovremenogRada vrstaPrekovremenogRada;

    @Column(name = "BrojDodatnihSati")
    private Double brojDodatnihSati;

    @Column(name = "DatumOd")
    private LocalDate datumOd;

    @Column(name = "DatumDo")
    private LocalDate datumDo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;

    @ManyToMany(mappedBy = "prekovremeniRad", fetch = FetchType.LAZY)
    private Set<IsplatnaListaPrekovremeniRad> isplatneListePrekovremeniRadovi;
}
