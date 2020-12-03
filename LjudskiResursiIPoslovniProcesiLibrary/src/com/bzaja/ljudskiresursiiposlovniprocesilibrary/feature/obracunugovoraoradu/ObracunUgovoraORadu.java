package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ObracunUgovoraORadu")
@PrimaryKeyJoinColumn(name = "IDObracunUgovoraORadu")
@Data
public class ObracunUgovoraORadu extends ObracunUgovora implements Serializable {

    @Column(name = "DatumOd")
    private LocalDate datumOd;

    @Column(name = "DatumDo")
    private LocalDate datumDo;

    @Column(name = "OsnovniOsobniOdbitak")
    private Double osnovniOsobniOdbitak;

    @Column(name = "OsnovicaOsobnogOdbitka")
    private Double osnovicaOsobnogOdbitka;
}
