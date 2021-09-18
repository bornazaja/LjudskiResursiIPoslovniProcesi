package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.Dodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
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
@Table(name = "IsplatnaListaDodatak")
@Data
public class IsplatnaListaDodatak implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIsplatnaListaDodatak")
    private Integer idIsplatnaListaDodatak;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IsplatnaListaID")
    private IsplatnaLista isplatnaLista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DodatakID")
    private Dodatak dodatak;
}
