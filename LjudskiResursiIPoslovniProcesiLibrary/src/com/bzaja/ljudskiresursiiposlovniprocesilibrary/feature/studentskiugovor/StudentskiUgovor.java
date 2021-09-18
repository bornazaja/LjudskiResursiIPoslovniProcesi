package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenik;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "StudentskiUgovor")
@PrimaryKeyJoinColumn(name = "IDStudentskiUgovor")
@Data
public class StudentskiUgovor extends Ugovor implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StudentskiPosaoCjenikID")
    private StudentskiPosaoCjenik studentskiPosaoCjenik;

    @Column(name = "BrojOdradjenihSati")
    private Double brojOdradjenihSati;

    @Column(name = "CijenaPoSatu")
    private Double cijenaPoSatu;

    @Column(name = "DosadZaradjeniIznosUOvojGodini")
    private Double dosadZaradjeniIznosUOvojGodini;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;
    
    @Column(name = "JeObracunat")
    private Boolean jeObracunat;
}
