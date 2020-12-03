package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu;

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
@Table(name = "UgovorORadu")
@PrimaryKeyJoinColumn(name = "IDUgovorORadu")
@Data
public class UgovorORadu extends Ugovor implements Serializable {

    @Column(name = "BrojRadnihSatiTjedno")
    private Double brojRadnihSatiTjedno;
    
    @Column(name = "BrutoPlaca")
    private Double brutoPlaca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ValutaID")
    private Valuta valuta;
}
