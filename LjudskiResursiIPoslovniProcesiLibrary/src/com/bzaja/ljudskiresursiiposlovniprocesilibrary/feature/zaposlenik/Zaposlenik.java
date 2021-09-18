package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.Davanje;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.Dodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.Obustava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.Olaksica;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.Prebivaliste;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol.Spol;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola.ZaposlenikRola;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Zaposlenik")
@Data
@EqualsAndHashCode(exclude = {"prebivalista", "ugovori", "zaposleniciRole", "davanja", "olaksice", "dodatci", "obustave", "prekovremeniRadovi", "povijestPrijava"})
public class Zaposlenik implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDZaposlenik")
    private Integer idZaposlenik;

    @Column(name = "Ime")
    private String ime;

    @Column(name = "Prezime")
    private String prezime;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SpolID")
    private Spol spol;

    @Column(name = "DatumRodjenja")
    private LocalDate datumRodjenja;

    @Column(name = "OIB")
    private String oib;
    
    @Column(name = "Email")
    private String email;

    @Column(name = "Lozinka")
    private String lozinka;

    @Column(name = "BrojTelefona")
    private String brojTelefona;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<Prebivaliste> prebivalista;
    
    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<Ugovor> ugovori;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<Dodatak> dodatci;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<Obustava> obustave;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<PrekovremeniRad> prekovremeniRadovi;

    @ManyToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<ZaposlenikRola> zaposleniciRole;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<Davanje> davanja;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<Olaksica> olaksice;

    @OneToMany(mappedBy = "zaposlenik", fetch = FetchType.LAZY)
    private Set<PovijestPrijava> povijestPrijava;
}
