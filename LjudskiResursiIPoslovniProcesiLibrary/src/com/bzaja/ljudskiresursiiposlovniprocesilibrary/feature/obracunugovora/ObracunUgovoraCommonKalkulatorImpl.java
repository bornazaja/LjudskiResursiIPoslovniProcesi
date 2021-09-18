package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje.IsplatnaListaDavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje.IsplatnaListaDavanjeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak.IsplatnaListaDodatakDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak.IsplatnaListaDodatakService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava.IsplatnaListaObustavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava.IsplatnaListaObustavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica.IsplatnaListaOlaksicaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica.IsplatnaListaOlaksicaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad.IsplatnaListaPrekovremeniRadDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad.IsplatnaListaPrekovremeniRadService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonKalkulator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PayrollCalculatorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraCommonKalkulatorImpl implements ObracunUgovoraCommonKalkulator {

    @Autowired
    private IsplatnaListaDavanjeService isplatnaListaDavanjeService;

    @Autowired
    private IsplatnaListaDodatakService isplatnaListaDodatakService;

    @Autowired
    private IsplatnaListaObustavaService isplatnaListaObustavaService;

    @Autowired
    private IsplatnaListaOlaksicaService isplatnaListaOlaksicaService;

    @Autowired
    private IsplatnaListaPrekovremeniRadService isplatnaListaPrekovremeniRadService;

    @Override
    public DavanjeResultListDto getDavanjeResultList(Integer idIsplatnaLista, Double osnovica) {
        List<IsplatnaListaDavanjeDto> isplatneListeDavanjaDto = isplatnaListaDavanjeService.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);

        if (!isplatneListeDavanjaDto.isEmpty()) {
            List<DavanjeDetailsDto> davanjaDetailsDto = isplatneListeDavanjaDto.stream().map(x -> {
                Double stopa = x.getStopaNaPlacu() == 0 ? x.getStopaIzPlace() : x.getStopaNaPlacu();
                Double iznosNaPlacu = x.getStopaNaPlacu() == 0 ? 0 : PayrollCalculatorUtils.fromStopaToIznosByOsnovica(osnovica, stopa);
                Double iznosIzPlacu = x.getStopaIzPlace() == 0 ? 0 : PayrollCalculatorUtils.fromStopaToIznosByOsnovica(osnovica, stopa);

                DavanjeDetailsDto davanjeDetailsDto = new DavanjeDetailsDto();
                davanjeDetailsDto.setNaziv(x.getDavanje().getVrstaDavanja().getNaziv());
                davanjeDetailsDto.setOsnovica(osnovica);
                davanjeDetailsDto.setStopaNaPlacu(x.getStopaNaPlacu());
                davanjeDetailsDto.setIznosNaPlacu(iznosNaPlacu);
                davanjeDetailsDto.setStopaIzPlace(x.getStopaIzPlace());
                davanjeDetailsDto.setIznosIzPlace(iznosIzPlacu);
                return davanjeDetailsDto;
            }).collect(Collectors.toList());

            Double ukupanIznosDavanjaNaPlacu = davanjaDetailsDto.stream().mapToDouble(x -> x.getIznosNaPlacu()).sum();
            Double ukupanIznosDavanjaIzPlace = davanjaDetailsDto.stream().filter(x -> x.getStopaIzPlace() > 0).mapToDouble(x -> x.getIznosIzPlace()).sum();

            return new DavanjeResultListDto(davanjaDetailsDto, ukupanIznosDavanjaNaPlacu, ukupanIznosDavanjaIzPlace);
        } else {
            return new DavanjeResultListDto(new ArrayList<>(), 0.0, 0.0);
        }
    }

    @Override
    public OlaksicaResultListDto getOlaksicaResultList(Integer idIsplatnaLista, Double osnovicaOsobnogOdbitka) {
        List<IsplatnaListaOlaksicaDto> isplatneListeOlaksice = isplatnaListaOlaksicaService.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);

        if (!isplatneListeOlaksice.isEmpty()) {
            List<OlaksicaDetailsDto> olaksiceDetailsDto = isplatneListeOlaksice.stream().map(x -> {
                Double iznos = x.getKoeficjent() * osnovicaOsobnogOdbitka;

                OlaksicaDetailsDto olaksicaDetailsDto = new OlaksicaDetailsDto();
                olaksicaDetailsDto.setNaziv(x.getOlaksica().getVrstaOlaksice().getNaziv());
                olaksicaDetailsDto.setOsnovica(osnovicaOsobnogOdbitka);
                olaksicaDetailsDto.setKoeficjent(x.getKoeficjent());
                olaksicaDetailsDto.setIznos(iznos);
                return olaksicaDetailsDto;
            }).collect(Collectors.toList());

            Double ukupanIznos = olaksiceDetailsDto.stream().mapToDouble(x -> x.getIznos()).sum();
            return new OlaksicaResultListDto(olaksiceDetailsDto, ukupanIznos);
        } else {
            return new OlaksicaResultListDto(new ArrayList<>(), 0.0);
        }
    }

    @Override
    public DodatakResultListDto getDodatakResultList(Integer idIsplatnaLista) {
        List<IsplatnaListaDodatakDto> isplatneListeDodatciDto = isplatnaListaDodatakService.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);

        if (!isplatneListeDodatciDto.isEmpty()) {
            List<DodatakDetailsDto> dodatciDetailsDto = isplatneListeDodatciDto.stream().map(x -> {
                DodatakDetailsDto dodatakDetailsDto = new DodatakDetailsDto();
                dodatakDetailsDto.setNaziv(String.format("%s - %s", x.getDodatak().getVrstaDodatka().getNaziv(), x.getDodatak().getNaziv()));
                dodatakDetailsDto.setIznos(x.getDodatak().getIznos());
                return dodatakDetailsDto;
            }).collect(Collectors.toList());

            Double ukupanIznos = dodatciDetailsDto.stream().mapToDouble(x -> x.getIznos()).sum();

            return new DodatakResultListDto(dodatciDetailsDto, ukupanIznos);
        } else {
            return new DodatakResultListDto(new ArrayList<>(), 0.0);
        }
    }

    @Override
    public ObustavaResultListDto getObustavaResultList(Integer idIsplatnaLista) {
        List<IsplatnaListaObustavaDto> isplatneListeObustaveDto = isplatnaListaObustavaService.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);

        if (!isplatneListeObustaveDto.isEmpty()) {
            List<ObustavaDetailsDto> obustaveDetailsDto = isplatneListeObustaveDto.stream().map(x -> {
                ObustavaDetailsDto obustavaDetailsDto = new ObustavaDetailsDto();
                obustavaDetailsDto.setNaziv(String.format("%s - %s", x.getObustava().getVrstaObustave().getNaziv(), x.getObustava().getNaziv()));
                obustavaDetailsDto.setIznos(x.getObustava().getIznos());

                return obustavaDetailsDto;
            }).collect(Collectors.toList());

            Double ukupanIznos = obustaveDetailsDto.stream().mapToDouble(x -> x.getIznos()).sum();

            return new ObustavaResultListDto(obustaveDetailsDto, ukupanIznos);
        } else {
            return new ObustavaResultListDto(new ArrayList<>(), 0.0);
        }
    }

    @Override
    public PrekovremeniRadResultListDto getPrekovremeniRadResultList(Integer idIsplatnaLista, Double cijenaPoSatu) {
        List<IsplatnaListaPrekovremeniRadDto> isplatneListePrekovremeniRadoviDto = isplatnaListaPrekovremeniRadService.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);

        if (!isplatneListePrekovremeniRadoviDto.isEmpty()) {
            List<PrekovremeniRadDetailsDto> prekovremeniRadoviDetailsDto = isplatneListePrekovremeniRadoviDto.stream().map(x -> {
                Double iznosPrekovremenogRada = PayrollCalculatorUtils.getIznosZaPrekovremeniRad(cijenaPoSatu, x.getPrekovremeniRad().getBrojDodatnihSati(), x.getKoeficjent());

                PrekovremeniRadDetailsDto prekovremeniRadDetailsDto = new PrekovremeniRadDetailsDto();
                prekovremeniRadDetailsDto.setNaziv(x.getPrekovremeniRad().getNaziv());
                prekovremeniRadDetailsDto.setKoeficjent(x.getKoeficjent());
                prekovremeniRadDetailsDto.setBrojDodatnihSati(x.getPrekovremeniRad().getBrojDodatnihSati());
                prekovremeniRadDetailsDto.setIznos(iznosPrekovremenogRada);
                return prekovremeniRadDetailsDto;
            }).collect(Collectors.toList());

            Double ukupanIznos = prekovremeniRadoviDetailsDto.stream().mapToDouble(x -> x.getIznos()).sum();

            return new PrekovremeniRadResultListDto(prekovremeniRadoviDetailsDto, ukupanIznos);
        } else {
            return new PrekovremeniRadResultListDto(new ArrayList<>(), 0.0);
        }
    }
}
