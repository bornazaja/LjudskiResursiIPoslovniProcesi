package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjelu;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraODjeluMakerImpl implements ObracunUgovoraODjeluMaker {

    @Autowired
    private UgovorODjeluService ugovorODjeluService;

    @Autowired
    private IsplatnaListaMaker isplatnaListaMaker;

    @Autowired
    private ObracunUgovoraCommonMaker obracunUgovoraCommonMaker;

    @Override
    public ObracunUgovoraODjelu make(AddObracunUgovoraODjeluDto addObracunUgovoraODjeluDto) {
        ObracunUgovoraODjelu obracunUgovoraODjelu = ObjectMapperUtils.map(addObracunUgovoraODjeluDto, ObracunUgovoraODjelu.class);

        List<UgovorODjelu> ugovoriODjelu = getUgovoriODjelu(addObracunUgovoraODjeluDto.getIdeviUgovora());
        Set<IsplatnaLista> isplatneListe = new LinkedHashSet<>();

        ugovoriODjelu.forEach((ugovorODjelu) -> {
            IsplatnaLista isplatnaLista = isplatnaListaMaker.make(obracunUgovoraODjelu, ugovorODjelu, ugovorODjelu.getDatumOd(), ugovorODjelu.getDatumDo());
            isplatneListe.add(isplatnaLista);
        });

        return (ObracunUgovoraODjelu) obracunUgovoraCommonMaker.make(obracunUgovoraODjelu, isplatneListe);
    }

    private List<UgovorODjelu> getUgovoriODjelu(List<Integer> ideviUgovora) {
        List<UgovorODjeluDto> ugovoriODjeluDto = ugovorODjeluService.findByIdUgovorIn(ideviUgovora);
        return ObjectMapperUtils.mapAll(ugovoriODjeluDto, UgovorODjelu.class);
    }
}
