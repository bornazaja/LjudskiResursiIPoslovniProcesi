package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraPdfReportFactory {

    @Autowired
    private ObracunUgovoraORaduPdfReport obracunUgovoraUgovorORaduPdfReport;

    @Autowired
    private ObracunUgovoraODjeluPdfReport obracunUgovoraUgovorODjeluPdfReport;

    @Autowired
    private ObracunStudentskihUgovoraPdfReport obracunUgovoraStudentskiUgovorPdfReport;

    public ObracunUgovoraPdfReport get(VrsteObracuna vrsteObracuna) {
        switch (vrsteObracuna) {
            case OBRACUN_UGOVORA_O_RADU:
                return obracunUgovoraUgovorORaduPdfReport;
            case OBRACUN_UGOVORA_O_DJELU:
                return obracunUgovoraUgovorODjeluPdfReport;
            case OBRACUN_STUDENTSKIH_UGOVORA:
                return obracunUgovoraStudentskiUgovorPdfReport;
            default:
                throw new IllegalArgumentException("Ovaj vrsta obračuna ne postoji.");
        }
    }
}
