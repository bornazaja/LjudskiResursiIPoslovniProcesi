package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.Zaposlenik;
import java.io.Serializable;
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
@Table(name = "Spol")
@Data
@EqualsAndHashCode(exclude = "zaposlenici")
public class Spol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSpol")
    private Integer idSpol;

    @Column(name = "Naziv")
    private String naziv;

    @OneToMany(mappedBy = "spol", fetch = FetchType.LAZY)
    private Set<Zaposlenik> zaposlenici;
}
