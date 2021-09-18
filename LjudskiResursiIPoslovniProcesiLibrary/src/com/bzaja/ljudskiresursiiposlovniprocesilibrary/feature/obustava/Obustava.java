package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava.IsplatnaListaObustava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustave;
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
@Table(name = "Obustava")
@Data
@EqualsAndHashCode(exclude = "isplatneListeObustave")
public class Obustava implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDObustava")
    private Integer idObustava;

    @Column(name = "Naziv")
    private String naziv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaObustaveID")
    private VrstaObustave vrstaObustave;

    @Column(name = "Iznos")
    private Double iznos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;

    @Column(name = "DatumOd")
    private LocalDate datumOd;

    @Column(name = "DatumDo")
    private LocalDate datumDo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;

    @ManyToMany(mappedBy = "obustava", fetch = FetchType.LAZY)
    private Set<IsplatnaListaObustava> isplatneListeObustave;
}
