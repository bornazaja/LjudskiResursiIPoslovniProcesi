package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.Obustava;
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
@Table(name = "IsplatnaListaObustava")
@Data
public class IsplatnaListaObustava implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIsplatnaListaObustava")
    private Integer idIsplatnaListaObustava;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IsplatnaListaID")
    private IsplatnaLista isplatnaLista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ObustavaID")
    private Obustava obustava;
}
