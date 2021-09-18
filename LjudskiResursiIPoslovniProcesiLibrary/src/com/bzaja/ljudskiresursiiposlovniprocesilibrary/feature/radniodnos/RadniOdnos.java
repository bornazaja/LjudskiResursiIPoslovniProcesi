package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora.VrstaUgovora;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "RadniOdnos")
@Data
@EqualsAndHashCode(exclude = "ugovori")
public class RadniOdnos implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRadniOdnos")
    private Integer idRadniOdnos;
    
    @Column(name = "Naziv")
    private String naziv;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VrstaUgovoraID")
    private VrstaUgovora vrstaUgovora;
    
    @OneToMany(mappedBy = "radniOdnos", fetch = FetchType.LAZY)
    private Set<Ugovor> ugovori;
}
