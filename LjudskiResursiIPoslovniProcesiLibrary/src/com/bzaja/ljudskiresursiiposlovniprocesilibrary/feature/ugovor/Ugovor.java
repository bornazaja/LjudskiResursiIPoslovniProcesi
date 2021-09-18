package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos.RadniOdnos;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjesto;
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Ugovor")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(exclude = "isplatneListe")
public class Ugovor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUgovor")
    private Integer idUgovor;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RadniOdnosID")
    private RadniOdnos radniOdnos;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RadnoMjestoID")
    private RadnoMjesto radnoMjesto;

    @Column(name = "DatumOd")
    private LocalDate datumOd;

    @Column(name = "DatumDo")
    private LocalDate datumDo;

    @OneToMany(mappedBy = "ugovor", fetch = FetchType.LAZY)
    private Set<IsplatnaLista> isplatneListe;
}
