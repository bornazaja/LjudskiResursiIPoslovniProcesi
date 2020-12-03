package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.Olaksica;
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
@Table(name = "IsplatnaListaOlaksica")
@Data
public class IsplatnaListaOlaksica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIsplatnaListaOlaksica")
    private Integer idIsplatnaListaOlaksica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IsplatnaListaID")
    private IsplatnaLista isplatnaLista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OlaksicaID")
    private Olaksica olaksica;

    @Column(name = "Koeficjent")
    private Double koeficjent;
}
