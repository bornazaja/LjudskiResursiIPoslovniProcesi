package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonKalkulator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez.ObracunUgovoraPorezService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PayrollCalculatorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraORaduKalkulatorImpl implements ObracunUgovoraORaduKalkulator {

    @Autowired
    private ObracunUgovoraORaduService obracunUgovoraORaduService;

    @Autowired
    private IsplatnaListaService isplatnaListaService;

    @Autowired
    private UgovorORaduService ugovorORaduService;

    @Autowired
    private ObracunUgovoraPorezService obracunUgovoraPorezService;

    @Autowired
    private PodaciOTvrtkiService podaciOTvrtkiService;

    @Autowired
    private ObracunUgovoraCommonKalkulator obracunUgovoraCommonKalkulator;

    private static final int BROJ_TJEDANA_U_MJESECU = 4;

    @Override
    public ObracunUgovoraORaduResultDto getResultByIdObracunUgovora(Integer idObracunUgovora) {
        ObracunUgovoraORaduDto obracunUgovoraORaduDto = obracunUgovoraORaduService.findById(idObracunUgovora);
        List<IsplatnaListaDto> isplatneListeDto = isplatnaListaService.findAllByIdObracunUgovora(idObracunUgovora);
        return getObracunUgovoraORaduResultDto(obracunUgovoraORaduDto, isplatneListeDto);
    }

    @Override
    public ObracunUgovoraORaduResultDto getResultByIdIsplatnaLista(Integer idIsplatnaLista) {
        Integer idObracunUgovoraORadu = isplatnaListaService.findById(idIsplatnaLista).getObracunUgovora().getIdObracunUgovora();
        ObracunUgovoraORaduDto obracunUgovoraORaduDto = obracunUgovoraORaduService.findById(idObracunUgovoraORadu);
        List<IsplatnaListaDto> isplatneListeDto = Arrays.asList(isplatnaListaService.findById(idIsplatnaLista));
        return getObracunUgovoraORaduResultDto(obracunUgovoraORaduDto, isplatneListeDto);
    }

    private ObracunUgovoraORaduResultDto getObracunUgovoraORaduResultDto(ObracunUgovoraORaduDto obracunUgovoraORaduDto, List<IsplatnaListaDto> isplatneListeDto) {
        List<IsplatnaListaUgovoraORaduDto> isplatneListeUgovoraORaduDto = new ArrayList<>();

        isplatneListeDto.forEach((isplatnaLista) -> {
            isplatneListeUgovoraORaduDto.add(getIsplatnaListaUgovoraORdu(obracunUgovoraORaduDto, isplatnaLista));
        });

        ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto = new ObracunUgovoraORaduResultDto();
        obracunUgovoraORaduResultDto.setVrstaObracuna(obracunUgovoraORaduDto.getVrstaObracuna());
        obracunUgovoraORaduResultDto.setOpis(obracunUgovoraORaduDto.getOpis());
        obracunUgovoraORaduResultDto.setDatumObracuna(obracunUgovoraORaduDto.getDatumObracuna());
        obracunUgovoraORaduResultDto.setValuta(obracunUgovoraORaduDto.getValuta());
        obracunUgovoraORaduResultDto.setOsnovniOsobniOdbitak(obracunUgovoraORaduDto.getOsnovniOsobniOdbitak());
        obracunUgovoraORaduResultDto.setOsnovicaOsobnogOdbitka(obracunUgovoraORaduDto.getOsnovicaOsobnogOdbitka());
        obracunUgovoraORaduResultDto.setPodaciOTvrtki(podaciOTvrtkiService.findFirst());
        obracunUgovoraORaduResultDto.setDatumOd(obracunUgovoraORaduDto.getDatumOd());
        obracunUgovoraORaduResultDto.setDatumDo(obracunUgovoraORaduDto.getDatumDo());
        obracunUgovoraORaduResultDto.setIsplatneListeUgovoraORadu(isplatneListeUgovoraORaduDto);
        return obracunUgovoraORaduResultDto;
    }

    private IsplatnaListaUgovoraORaduDto getIsplatnaListaUgovoraORdu(ObracunUgovoraORaduDto obracunUgovoraORaduDto, IsplatnaListaDto isplatnaLista) {
        UgovorORaduDto ugovorORaduDto = ugovorORaduService.findById(isplatnaLista.getUgovor().getIdUgovor());

        Double bruto = ugovorORaduDto.getBrutoPlaca();

        DavanjeResultListDto davanjeResultListDto = obracunUgovoraCommonKalkulator.getDavanjeResultList(isplatnaLista.getIdIsplatnaLista(), bruto);
        Double ukupanTrosak = bruto + davanjeResultListDto.getUkupanIznosDavanjaNaPlacu();
        Double dohodak = bruto - davanjeResultListDto.getUkupanIznosDavanjaIzPlace();

        OlaksicaResultListDto olaksicaResultListDto = obracunUgovoraCommonKalkulator.getOlaksicaResultList(isplatnaLista.getIdIsplatnaLista(), obracunUgovoraORaduDto.getOsnovicaOsobnogOdbitka());
        Double osobniOdbitak = obracunUgovoraORaduDto.getOsnovniOsobniOdbitak() + olaksicaResultListDto.getUkupanIznos();

        Double brojSatiMjesecno = ugovorORaduDto.getBrojRadnihSatiTjedno() * BROJ_TJEDANA_U_MJESECU;
        Double cijenaPoSatu = bruto / brojSatiMjesecno;

        DodatakResultListDto dodatakResultListDto = obracunUgovoraCommonKalkulator.getDodatakResultList(obracunUgovoraORaduDto.getIdObracunUgovora());
        ObustavaResultListDto obustavaResultListDto = obracunUgovoraCommonKalkulator.getObustavaResultList(obracunUgovoraORaduDto.getIdObracunUgovora());
        PrekovremeniRadResultListDto prekovremeniRadResultListDto = obracunUgovoraCommonKalkulator.getPrekovremeniRadResultList(obracunUgovoraORaduDto.getIdObracunUgovora(), cijenaPoSatu);

        Double ukupanIznosDodatakaObustavaIPrekovremenihRadova = dodatakResultListDto.getUkupanIznos() + prekovremeniRadResultListDto.getUkupanIznos() - obustavaResultListDto.getUkupanIznos();

        Double ukupnoPorez = 0.0;
        Double stopaPoreza = 0.0;
        Double stopaPrireza = 0.0;
        Double iznosPoreza = 0.0;
        Double iznosPrireza = 0.0;
        Double porezIPrirezUkupno = 0.0;
        Double neto = dohodak;

        if (dohodak > osobniOdbitak) {
            ukupnoPorez = dohodak - osobniOdbitak;
            stopaPoreza = obracunUgovoraPorezService.findStopaByObracunUgovoraIdAndDohodak(obracunUgovoraORaduDto.getIdObracunUgovora(), dohodak);
            stopaPrireza = isplatnaLista.getPrirez();
            iznosPoreza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(ukupnoPorez, stopaPoreza);
            iznosPrireza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(iznosPoreza, stopaPrireza);
            porezIPrirezUkupno = iznosPoreza + iznosPrireza;
            neto = dohodak - porezIPrirezUkupno;
        }

        Double iznosZaIsplatu = neto + ukupanIznosDodatakaObustavaIPrekovremenihRadova;

        ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto = getBasicZaposlenikDetailsDto(ugovorORaduDto, isplatnaLista);
        UgovorORaduDetailsDto ugovorORaduDetailsDto = getUgovorORaduDetailsDto(ugovorORaduDto);

        IsplatnaListaUgovoraORaduDto isplatnaListaUgovoraORaduDto = new IsplatnaListaUgovoraORaduDto();
        isplatnaListaUgovoraORaduDto.setZaposlenikBasicDetails(zaposlenikBasicDetailsDto);
        isplatnaListaUgovoraORaduDto.setUgovorORaduDetails(ugovorORaduDetailsDto);
        isplatnaListaUgovoraORaduDto.setDavanjeResultList(davanjeResultListDto);
        isplatnaListaUgovoraORaduDto.setUkupanTrosak(ukupanTrosak);
        isplatnaListaUgovoraORaduDto.setBruto(bruto);
        isplatnaListaUgovoraORaduDto.setDohodak(dohodak);
        isplatnaListaUgovoraORaduDto.setOlaksicaResultList(olaksicaResultListDto);
        isplatnaListaUgovoraORaduDto.setOsobniOdbitak(osobniOdbitak);
        isplatnaListaUgovoraORaduDto.setDodatakResultList(dodatakResultListDto);
        isplatnaListaUgovoraORaduDto.setObustavaResultList(obustavaResultListDto);
        isplatnaListaUgovoraORaduDto.setPrekovremeniRadResultList(prekovremeniRadResultListDto);
        isplatnaListaUgovoraORaduDto.setUkupnoPorez(ukupnoPorez);
        isplatnaListaUgovoraORaduDto.setStopaPoreza(stopaPoreza);
        isplatnaListaUgovoraORaduDto.setStopaPrireza(stopaPrireza);
        isplatnaListaUgovoraORaduDto.setIznosPoreza(iznosPoreza);
        isplatnaListaUgovoraORaduDto.setIznosPrireza(iznosPrireza);
        isplatnaListaUgovoraORaduDto.setPorezIPrirezUkupno(porezIPrirezUkupno);
        isplatnaListaUgovoraORaduDto.setNeto(neto);
        isplatnaListaUgovoraORaduDto.setIznosZaIsplatu(iznosZaIsplatu);
        return isplatnaListaUgovoraORaduDto;
    }

    private ZaposlenikBasicDetailsDto getBasicZaposlenikDetailsDto(UgovorORaduDto ugovorORaduDto, IsplatnaListaDto isplatnaLista) {
        ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto = new ZaposlenikBasicDetailsDto();
        zaposlenikBasicDetailsDto.setImeIPrezime(String.format("%s %s", ugovorORaduDto.getZaposlenik().getIme(), ugovorORaduDto.getZaposlenik().getPrezime()));
        zaposlenikBasicDetailsDto.setOib(ugovorORaduDto.getZaposlenik().getOib());
        zaposlenikBasicDetailsDto.setPrebivaliste(String.format("%s, %s", isplatnaLista.getPrebivaliste().getUlica(), isplatnaLista.getPrebivaliste().getGrad().getNaziv()));
        return zaposlenikBasicDetailsDto;
    }

    private UgovorORaduDetailsDto getUgovorORaduDetailsDto(UgovorORaduDto ugovorORaduDto) {
        UgovorORaduDetailsDto ugovorORaduDetailsDto = new UgovorORaduDetailsDto();
        ugovorORaduDetailsDto.setVrstaUgovora(ugovorORaduDto.getRadniOdnos().getVrstaUgovora().getNaziv());
        ugovorORaduDetailsDto.setRadniOdnos(ugovorORaduDto.getRadniOdnos().getNaziv());
        ugovorORaduDetailsDto.setRadnoMjesto(ugovorORaduDto.getRadnoMjesto().getNaziv());
        ugovorORaduDetailsDto.setDatumOd(ugovorORaduDto.getDatumOd());
        ugovorORaduDetailsDto.setDatumDo(ugovorORaduDto.getDatumDo());
        ugovorORaduDetailsDto.setBrojRadnihSatiTjedno(ugovorORaduDto.getBrojRadnihSatiTjedno());
        ugovorORaduDetailsDto.setBrutoPlaca(ugovorORaduDto.getBrutoPlaca());
        return ugovorORaduDetailsDto;
    }
}
