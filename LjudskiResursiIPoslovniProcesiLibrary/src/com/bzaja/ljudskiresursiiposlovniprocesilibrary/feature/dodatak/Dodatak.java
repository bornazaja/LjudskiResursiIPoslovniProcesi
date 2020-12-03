package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak.IsplatnaListaDodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatka;
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
@Table(name = "Dodatak")
@Data
@EqualsAndHashCode(exclude = "isplatneListeDodatci")
public class Dodatak implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDodatak")
    private Integer idDodatak;

    @Column(name = "Naziv")
    private String naziv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaDodatkaID")
    private VrstaDodatka vrstaDodatka;

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

    @ManyToMany(mappedBy = "dodatak", fetch = FetchType.LAZY)
    private Set<IsplatnaListaDodatak> isplatneListeDodatci;
}
