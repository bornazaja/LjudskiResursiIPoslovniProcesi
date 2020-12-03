package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje.IsplatnaListaDavanje;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanja;
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
@Table(name = "Davanje")
@Data
@EqualsAndHashCode(exclude = "isplatneListeDavanja")
public class Davanje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDavanje")
    private Integer idDavanje;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaDavanjaID")
    private VrstaDavanja vrstaDavanja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;
    
    @Column(name = "DatumOd")
    private LocalDate datumOd;
    
    @Column(name = "DatumDo")
    private LocalDate datumDo;
    
    @ManyToMany(mappedBy = "davanje", fetch = FetchType.LAZY)
    private Set<IsplatnaListaDavanje> isplatneListeDavanja;
}
