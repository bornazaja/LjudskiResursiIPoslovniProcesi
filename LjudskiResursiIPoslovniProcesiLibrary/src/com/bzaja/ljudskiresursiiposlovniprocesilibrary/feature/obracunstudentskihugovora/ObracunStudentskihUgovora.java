package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ObracunStudentskihUgovora")
@PrimaryKeyJoinColumn(name = "IDObracunStudentskihUgovora")
@Data
public class ObracunStudentskihUgovora extends ObracunUgovora implements Serializable {

    @Column(columnDefinition = "LimitGodisnjegPrihodaZaStudenta")
    private Double limitGodisnjegIznosaZaStudenta;
}
