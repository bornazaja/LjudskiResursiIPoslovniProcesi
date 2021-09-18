package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.exception.customexception.InvalidObracunUgovoraException;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.Davanje;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.Dodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje.IsplatnaListaDavanje;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak.IsplatnaListaDodatak;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava.IsplatnaListaObustava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica.IsplatnaListaOlaksica;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad.IsplatnaListaPrekovremeniRad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.Obustava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.Olaksica;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.Prebivaliste;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRad;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.Ugovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IsplatnaListaMakerImpl implements IsplatnaListaMaker {

    @Autowired
    private DavanjeService davanjeService;

    @Autowired
    private DodatakService dodatakService;

    @Autowired
    private ObustavaService obustavaService;

    @Autowired
    private OlaksicaService olaksicaService;

    @Autowired
    private PrekovremeniRadService prekovremeniRadService;

    @Autowired
    private PrebivalisteService prebivalisteService;

    @Override
    public IsplatnaLista make(ObracunUgovora obracunUgovora, Ugovor ugovor, LocalDate datumOd, LocalDate datumDo) {
        Integer idZaposlenik = ugovor.getZaposlenik().getIdZaposlenik();
        Prebivaliste prebivaliste = getPrebivaliste(idZaposlenik);

        if (prebivaliste != null) {
            IsplatnaLista isplatnaLista = new IsplatnaLista();
            isplatnaLista.setObracunUgovora(obracunUgovora);
            isplatnaLista.setUgovor(ugovor);
            isplatnaLista.setPrebivaliste(prebivaliste);
            isplatnaLista.setPrirez(prebivaliste.getGrad().getPrirez());
            isplatnaLista.setIsplatneListeDavanja(getIsplatneListeDavanja(isplatnaLista, datumOd, datumDo, idZaposlenik));
            isplatnaLista.setIsplatneListeDodatci(getIsplatneListeDodatci(isplatnaLista, datumOd, datumDo, idZaposlenik));
            isplatnaLista.setIsplatneListeObustave(getIsplatneListeObustave(isplatnaLista, datumOd, datumDo, idZaposlenik));
            isplatnaLista.setIsplatneListeOlaksice(getIsplatneListeOlakisce(isplatnaLista, datumOd, datumDo, idZaposlenik));
            isplatnaLista.setIsplatneListePrekovremeniRadovi(getIsplatneListePrekovremeniRadovi(isplatnaLista, datumOd, datumDo, idZaposlenik));
            return isplatnaLista;
        } else {
            throw new InvalidObracunUgovoraException(String.format("Zaposlenik s ID-em: \"%d\" nema definirano prebivalište, te zbog tog nije moguće izvršiti obračun.", idZaposlenik));
        }
    }

    private Prebivaliste getPrebivaliste(Integer idZaposlenik) {
        PrebivalisteDto prebivalisteDto = prebivalisteService.findFirstByZaposlenikIdZaposlenikOrderByDatumOdDesc(idZaposlenik);
        return ObjectMapperUtils.map(prebivalisteDto, Prebivaliste.class);
    }

    private Set<IsplatnaListaDavanje> getIsplatneListeDavanja(IsplatnaLista isplatnaLista, LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<DavanjeDto> davanjaDto = davanjeService.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        List<Davanje> davanja = ObjectMapperUtils.mapAll(davanjaDto, Davanje.class);

        Set<IsplatnaListaDavanje> isplatneListeDavanja = davanja.stream().map(x -> {
            IsplatnaListaDavanje isplatnaListaDavanje = new IsplatnaListaDavanje();
            isplatnaListaDavanje.setIsplatnaLista(isplatnaLista);
            isplatnaListaDavanje.setDavanje(x);
            isplatnaListaDavanje.setStopaNaPlacu(x.getVrstaDavanja().getStopaNaPlacu());
            isplatnaListaDavanje.setStopaIzPlace(x.getVrstaDavanja().getStopaIzPlace());
            return isplatnaListaDavanje;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        return isplatneListeDavanja;
    }

    private Set<IsplatnaListaDodatak> getIsplatneListeDodatci(IsplatnaLista isplatnaLista, LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<DodatakDto> dodatciDto = dodatakService.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        List<Dodatak> dodatci = ObjectMapperUtils.mapAll(dodatciDto, Dodatak.class);

        Set<IsplatnaListaDodatak> isplatneListeDodatci = dodatci.stream().map(x -> {
            IsplatnaListaDodatak isplatnaListaDodatak = new IsplatnaListaDodatak();
            isplatnaListaDodatak.setIsplatnaLista(isplatnaLista);
            isplatnaListaDodatak.setDodatak(x);
            return isplatnaListaDodatak;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        return isplatneListeDodatci;
    }

    private Set<IsplatnaListaObustava> getIsplatneListeObustave(IsplatnaLista isplatnaLista, LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<ObustavaDto> obustaveDto = obustavaService.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        List<Obustava> obustave = ObjectMapperUtils.mapAll(obustaveDto, Obustava.class);

        Set<IsplatnaListaObustava> isplatneListeObustave = obustave.stream().map(x -> {
            IsplatnaListaObustava isplatnaListaObustava = new IsplatnaListaObustava();
            isplatnaListaObustava.setIsplatnaLista(isplatnaLista);
            isplatnaListaObustava.setObustava(x);
            return isplatnaListaObustava;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        return isplatneListeObustave;
    }

    private Set<IsplatnaListaOlaksica> getIsplatneListeOlakisce(IsplatnaLista isplatnaLista, LocalDate datumOd, LocalDate datumDo, Integer idZaposlenik) {
        List<OlaksicaDto> olaksiceDto = olaksicaService.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZaposlenik);
        List<Olaksica> olaksice = ObjectMapperUtils.mapAll(olaksiceDto, Olaksica.class);

        Set<IsplatnaListaOlaksica> isplatneListeOlaksice = olaksice.stream().map(x -> {
            IsplatnaListaOlaksica isplatnaListaOlaksica = new IsplatnaListaOlaksica();
            isplatnaListaOlaksica.setIsplatnaLista(isplatnaLista);
            isplatnaListaOlaksica.setOlaksica(x);
            isplatnaListaOlaksica.setKoeficjent(x.getVrstaOlaksice().getKoeficjent());
            return isplatnaListaOlaksica;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        return isplatneListeOlaksice;
    }

    private Set<IsplatnaListaPrekovremeniRad> getIsplatneListePrekovremeniRadovi(IsplatnaLista isplatnaLista, LocalDate datumOd, LocalDate datumDo, Integer idZapolsenik) {
        List<PrekovremeniRadDto> prekovremeniRadoviDto = prekovremeniRadService.findAllInPeriodDatumOdAndDatumDoByZaposlenikId(datumOd, datumDo, idZapolsenik);
        List<PrekovremeniRad> prekovremeniRadovi = ObjectMapperUtils.mapAll(prekovremeniRadoviDto, PrekovremeniRad.class);

        Set<IsplatnaListaPrekovremeniRad> isplatneListePrekovremeniRadovi = prekovremeniRadovi.stream().map(x -> {
            IsplatnaListaPrekovremeniRad isplatnaListaPrekovremeniRad = new IsplatnaListaPrekovremeniRad();
            isplatnaListaPrekovremeniRad.setIsplatnaLista(isplatnaLista);
            isplatnaListaPrekovremeniRad.setPrekovremeniRad(x);
            isplatnaListaPrekovremeniRad.setKoeficjent(x.getVrstaPrekovremenogRada().getKoeficjent());
            return isplatnaListaPrekovremeniRad;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        return isplatneListePrekovremeniRadovi;
    }
}
