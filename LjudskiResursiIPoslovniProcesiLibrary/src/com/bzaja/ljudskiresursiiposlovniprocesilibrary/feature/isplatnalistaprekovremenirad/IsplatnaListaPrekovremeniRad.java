package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRad;
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
@Table(name = "IsplatnaListaPrekovremeniRad")
@Data
public class IsplatnaListaPrekovremeniRad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIsplatnaListaPrekovremeniRad")
    private Integer idIsplatnaListaPrekovremeniRad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IsplatnaListaID")
    private IsplatnaLista isplatnaLista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PrekovremeniRadID")
    private PrekovremeniRad prekovremeniRad;
    
    @Column(name = "Koeficjent")
    private Double koeficjent;
}
