package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.Valuta;
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
@Table(name = "StudentskiPosaoCjenik")
@Data
@EqualsAndHashCode(exclude = "studentskiUgovori")
public class StudentskiPosaoCjenik implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDStudentskiPosaoCjenik")
    private Integer idStudentskiPosaoCjenik;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "CijenaPoSatu")
    private Double cijenaPoSatu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;

    @OneToMany(mappedBy = "studentskiPosaoCjenik", fetch = FetchType.LAZY)
    private Set<StudentskiUgovor> studentskiUgovori;
}
