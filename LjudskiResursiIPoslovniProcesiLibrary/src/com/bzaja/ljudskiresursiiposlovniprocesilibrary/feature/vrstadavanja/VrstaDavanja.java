package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.Davanje;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "VrstaDavanja")
@Data
@EqualsAndHashCode(exclude = "davanja")
public class VrstaDavanja implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVrstaDavanja")
    private Integer idVrstaDavanja;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "StopaNaPlacu")
    private Double stopaNaPlacu;
    
    @Column(name = "StopaIzPlace")
    private Double stopaIzPlace;

    @Column(name = "VrijediDo")
    private LocalDate vrijediDo;

    @OneToMany(mappedBy = "vrstaDavanja", fetch = FetchType.LAZY)
    private Set<Davanje> davanja;
}
