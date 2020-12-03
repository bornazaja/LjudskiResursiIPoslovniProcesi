package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.Zaposlenik;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Rola;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ZaposlenikRola")
@Data
public class ZaposlenikRola implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDZaposlenikRola")
    private Integer idZaposlenikRola;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RolaID")
    private Rola rola;
}
