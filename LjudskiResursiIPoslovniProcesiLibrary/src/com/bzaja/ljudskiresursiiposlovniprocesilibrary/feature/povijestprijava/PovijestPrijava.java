package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Rola;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.Zaposlenik;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "PovijestPrijava")
@Data
public class PovijestPrijava implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPovijestPrijava")
    private Integer idPovijestPrijava;

    @Column(name = "VrijemePrijave")
    private LocalDateTime vrijemePrijave;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ZaposlenikID")
    private Zaposlenik zaposlenik;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RolaID")
    private Rola rola;
}
