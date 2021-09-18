package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.exception.customexception.InvalidObracunUgovoraException;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORadu;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraORaduMakerImpl implements ObracunUgovoraORaduMaker {

    @Autowired
    private UgovorORaduService ugovorORaduService;

    @Autowired
    private IsplatnaListaMaker isplatnaListaMaker;

    @Autowired
    private ObracunUgovoraCommonMaker obracunUgovoraCommonMaker;

    @Override
    public ObracunUgovoraORadu make(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        ObracunUgovoraORadu obracunUgovoraORadu = ObjectMapperUtils.map(obracunUgovoraORaduDto, ObracunUgovoraORadu.class);
        List<UgovorORadu> ugovoriORadu = getUgovoriORadu(obracunUgovoraORadu);

        if (ugovoriORadu.isEmpty()) {
            throw new InvalidObracunUgovoraException("Trenutno ne postoji niti jedan aktivni ugovor o radu, pa se stoga isti ne može izvršiti.");
        }

        Set<IsplatnaLista> isplatneListe = new LinkedHashSet<>();

        ugovoriORadu.forEach((ugovorORadu) -> {
            IsplatnaLista isplatnaLista = isplatnaListaMaker.make(obracunUgovoraORadu, ugovorORadu, obracunUgovoraORadu.getDatumOd(), obracunUgovoraORadu.getDatumDo());
            isplatneListe.add(isplatnaLista);
        });
        return (ObracunUgovoraORadu) obracunUgovoraCommonMaker.make(obracunUgovoraORadu, isplatneListe);
    }

    private List<UgovorORadu> getUgovoriORadu(ObracunUgovoraORadu obracunUgovoraORadu) {
        List<UgovorORaduDto> ugovoriORaduDto = ugovorORaduService.findAllInPeriodDatumOdAndDatumDo(obracunUgovoraORadu.getDatumOd(), obracunUgovoraORadu.getDatumDo());
        return ObjectMapperUtils.mapAll(ugovoriORaduDto, UgovorORadu.class);
    }
}
