package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ObracunUgovoraODjelu")
@PrimaryKeyJoinColumn(name = "IDObracunUgovoraODjelu")
@Data
public class ObracunUgovoraODjelu extends ObracunUgovora implements Serializable {

}
