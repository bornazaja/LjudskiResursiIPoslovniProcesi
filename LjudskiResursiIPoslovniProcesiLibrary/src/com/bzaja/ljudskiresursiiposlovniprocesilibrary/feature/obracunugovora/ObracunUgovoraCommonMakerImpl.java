package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez.ObracunUgovoraPorez;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.Porez;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza.VrstePoreza;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraCommonMakerImpl implements ObracunUgovoraCommonMaker {

    @Autowired
    private PorezService porezService;

    @Override
    public ObracunUgovora make(ObracunUgovora obracunUgovora, Set<IsplatnaLista> isplatneListe) {
        obracunUgovora.setIsplatneListe(isplatneListe);
        obracunUgovora.setObracuniUgovoraPorezi(getObracuniUgovoraPorezi(obracunUgovora));
        return obracunUgovora;
    }

    private Set<ObracunUgovoraPorez> getObracuniUgovoraPorezi(ObracunUgovora obracunUgovora) {

        Set<ObracunUgovoraPorez> obracuniUgovoraPorezi = new LinkedHashSet<>();

        if (obracunUgovora.getVrstaObracuna().getIdVrstaObracuna().equals(VrsteObracuna.OBRACUN_UGOVORA_O_RADU.getId())) {
            List<PorezDto> poreziDto = porezService.findAllByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrue(VrstePoreza.POREZ_NA_DOHODAK.getId());
            List<Porez> porezi = ObjectMapperUtils.mapAll(poreziDto, Porez.class);
            obracuniUgovoraPorezi = porezi.stream().map(x -> {
                ObracunUgovoraPorez obracunUgovoraPorez = new ObracunUgovoraPorez();
                obracunUgovoraPorez.setObracunUgovora(obracunUgovora);
                obracunUgovoraPorez.setPorez(x);
                obracunUgovoraPorez.setStopa(x.getStopa());
                obracunUgovoraPorez.setMinOsnovica(x.getMinOsnovica());
                obracunUgovoraPorez.setMaxOsnovica(x.getMaxOsnovica());
                return obracunUgovoraPorez;
            }).collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            PorezDto porezDto = porezService.findFirstByVrstaPorezaIdVrstaPorezaAndDrzavaJeDomovinaTrueOrderByStopaAsc(VrstePoreza.POREZ_NA_DOHODAK.getId());
            Porez porez = ObjectMapperUtils.map(porezDto, Porez.class);
            ObracunUgovoraPorez obracunUgovoraPorez = new ObracunUgovoraPorez();
            obracunUgovoraPorez.setObracunUgovora(obracunUgovora);
            obracunUgovoraPorez.setPorez(porez);
            obracunUgovoraPorez.setStopa(porez.getStopa());
            obracunUgovoraPorez.setMinOsnovica(0.0);
            obracunUgovoraPorez.setMaxOsnovica(null);
            obracuniUgovoraPorezi.add(obracunUgovoraPorez);
        }

        return obracuniUgovoraPorezi;
    }
}
