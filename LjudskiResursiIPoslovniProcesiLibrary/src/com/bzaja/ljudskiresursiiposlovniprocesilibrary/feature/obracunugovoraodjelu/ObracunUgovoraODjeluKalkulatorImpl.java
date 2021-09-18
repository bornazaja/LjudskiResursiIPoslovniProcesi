package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonKalkulator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez.ObracunUgovoraPorezService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PayrollCalculatorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraODjeluKalkulatorImpl implements ObracunUgovoraODjeluKalkulator {

    @Autowired
    private ObracunUgovoraODjeluService obracunUgovoraODjeluService;

    @Autowired
    private IsplatnaListaService isplatnaListaService;

    @Autowired
    private UgovorODjeluService ugovorODjeluService;

    @Autowired
    private ObracunUgovoraPorezService obracunUgovoraPorezService;

    @Autowired
    private PodaciOTvrtkiService podaciOTvrtkiService;

    @Autowired
    private ObracunUgovoraCommonKalkulator obracunUgovoraCommonKalkulator;

    @Override
    public ObracunUgovoraODjeluResultDto getResultByIdObracunUgovora(Integer idObracunUgovora) {
        ObracunUgovoraODjeluDto obracunUgovoraODjeluDto = obracunUgovoraODjeluService.findById(idObracunUgovora);
        List<IsplatnaListaDto> isplatneListeDto = isplatnaListaService.findAllByIdObracunUgovora(idObracunUgovora);
        return getObracunUgovoraODjeluResultDto(obracunUgovoraODjeluDto, isplatneListeDto);
    }

    @Override
    public ObracunUgovoraODjeluResultDto getResultByIdIsplatnaLista(Integer idIsplatnaLista) {
        Integer idObracunUgovoraODjelu = isplatnaListaService.findById(idIsplatnaLista).getObracunUgovora().getIdObracunUgovora();
        ObracunUgovoraODjeluDto obracunUgovoraODjeluDto = obracunUgovoraODjeluService.findById(idObracunUgovoraODjelu);
        List<IsplatnaListaDto> isplatneListeDto = Arrays.asList(isplatnaListaService.findById(idIsplatnaLista));
        return getObracunUgovoraODjeluResultDto(obracunUgovoraODjeluDto, isplatneListeDto);
    }

    private ObracunUgovoraODjeluResultDto getObracunUgovoraODjeluResultDto(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto, List<IsplatnaListaDto> isplatneListeDto) {
        List<IsplatnaListaUgovoraODjeluDto> isplatneListeUgovoraODjeluDto = new ArrayList<>();

        isplatneListeDto.forEach((isplatnaLista) -> {
            isplatneListeUgovoraODjeluDto.add(getIsplatnaListaUgovorODjelu(obracunUgovoraODjeluDto, isplatnaLista));
        });

        ObracunUgovoraODjeluResultDto obracunUgovoraODjeluResultDto = new ObracunUgovoraODjeluResultDto();
        obracunUgovoraODjeluResultDto.setVrstaObracuna(obracunUgovoraODjeluDto.getVrstaObracuna());
        obracunUgovoraODjeluResultDto.setOpis(obracunUgovoraODjeluDto.getOpis());
        obracunUgovoraODjeluResultDto.setDatumObracuna(obracunUgovoraODjeluDto.getDatumObracuna());
        obracunUgovoraODjeluResultDto.setValuta(obracunUgovoraODjeluDto.getValuta());
        obracunUgovoraODjeluResultDto.setPodaciOTvrtki(podaciOTvrtkiService.findFirst());
        obracunUgovoraODjeluResultDto.setIsplatneListeUgovoraODjelu(isplatneListeUgovoraODjeluDto);
        return obracunUgovoraODjeluResultDto;
    }

    private IsplatnaListaUgovoraODjeluDto getIsplatnaListaUgovorODjelu(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto, IsplatnaListaDto isplatnaLista) {
        UgovorODjeluDto ugovorODjeluDto = ugovorODjeluService.findById(isplatnaLista.getUgovor().getIdUgovor());

        Double bruto = ugovorODjeluDto.getBrutoIznos();
        Double iznosPausalnogPriznatogTroska = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(bruto, ugovorODjeluDto.getStopaPausalnogPriznatogTroska());
        Double brutoIznosNakonPausalogPriznatogTroska = bruto - iznosPausalnogPriznatogTroska;

        DavanjeResultListDto davanjeResultListDto = obracunUgovoraCommonKalkulator.getDavanjeResultList(isplatnaLista.getIdIsplatnaLista(), brutoIznosNakonPausalogPriznatogTroska);
        Double ukupniTrosak = bruto + davanjeResultListDto.getUkupanIznosDavanjaNaPlacu();
        Double dohodak = bruto - davanjeResultListDto.getUkupanIznosDavanjaIzPlace();

        DodatakResultListDto dodatakResultListDto = obracunUgovoraCommonKalkulator.getDodatakResultList(obracunUgovoraODjeluDto.getIdObracunUgovora());
        ObustavaResultListDto obustavaResultListDto = obracunUgovoraCommonKalkulator.getObustavaResultList(obracunUgovoraODjeluDto.getIdObracunUgovora());
        Double ukupanIznosDodatakaIObustava = dodatakResultListDto.getUkupanIznos() - obustavaResultListDto.getUkupanIznos();

        Double ukupnoPorez = dohodak - iznosPausalnogPriznatogTroska;
        Double stopaPoreza = obracunUgovoraPorezService.findStopaByObracunUgovoraId(obracunUgovoraODjeluDto.getIdObracunUgovora());
        Double stopaPrireza = isplatnaLista.getPrirez();
        Double iznosPoreza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(ukupnoPorez, stopaPoreza);
        Double iznosPrirza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(iznosPoreza, stopaPrireza);
        Double porezIPrirezUkupno = iznosPoreza + iznosPrirza;
        Double neto = dohodak - porezIPrirezUkupno;
        Double iznosZaIsplatu = neto + ukupanIznosDodatakaIObustava;

        ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto = getZaposlenikBasicDetailsDto(ugovorODjeluDto, isplatnaLista);
        UgovorODjeluDetailsDto ugovorODjeluDetailsDto = getUgovorODjeluDetailsDto(ugovorODjeluDto);

        IsplatnaListaUgovoraODjeluDto isplatnaListaUgovoraODjeluDto = new IsplatnaListaUgovoraODjeluDto();
        isplatnaListaUgovoraODjeluDto.setZaposlenikBasicDetails(zaposlenikBasicDetailsDto);
        isplatnaListaUgovoraODjeluDto.setUgovorODjeluDetails(ugovorODjeluDetailsDto);
        isplatnaListaUgovoraODjeluDto.setDavanjeResultList(davanjeResultListDto);
        isplatnaListaUgovoraODjeluDto.setDodatakResultList(dodatakResultListDto);
        isplatnaListaUgovoraODjeluDto.setObustavaResultList(obustavaResultListDto);
        isplatnaListaUgovoraODjeluDto.setBruto(bruto);
        isplatnaListaUgovoraODjeluDto.setIznosPausalnogPriznatogTroska(iznosPausalnogPriznatogTroska);
        isplatnaListaUgovoraODjeluDto.setUkupanTrosak(ukupniTrosak);
        isplatnaListaUgovoraODjeluDto.setDohodak(dohodak);
        isplatnaListaUgovoraODjeluDto.setUkupnoPorez(ukupnoPorez);
        isplatnaListaUgovoraODjeluDto.setStopaPoreza(stopaPoreza);
        isplatnaListaUgovoraODjeluDto.setStopaPrireza(stopaPrireza);
        isplatnaListaUgovoraODjeluDto.setIznosPoreza(iznosPoreza);
        isplatnaListaUgovoraODjeluDto.setIznosPrireza(iznosPrirza);
        isplatnaListaUgovoraODjeluDto.setPorezIPrirezUkupno(porezIPrirezUkupno);
        isplatnaListaUgovoraODjeluDto.setNeto(neto);
        isplatnaListaUgovoraODjeluDto.setIznosZaIsplatu(iznosZaIsplatu);
        return isplatnaListaUgovoraODjeluDto;
    }

    private ZaposlenikBasicDetailsDto getZaposlenikBasicDetailsDto(UgovorODjeluDto ugovorODjeluDto, IsplatnaListaDto isplatnaLista) {
        ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto = new ZaposlenikBasicDetailsDto();
        zaposlenikBasicDetailsDto.setImeIPrezime(String.format("%s %s", ugovorODjeluDto.getZaposlenik().getIme(), ugovorODjeluDto.getZaposlenik().getPrezime()));
        zaposlenikBasicDetailsDto.setOib(ugovorODjeluDto.getZaposlenik().getOib());
        zaposlenikBasicDetailsDto.setPrebivaliste(String.format("%s, %s", isplatnaLista.getPrebivaliste().getUlica(), isplatnaLista.getPrebivaliste().getGrad().getNaziv()));
        return zaposlenikBasicDetailsDto;
    }

    private UgovorODjeluDetailsDto getUgovorODjeluDetailsDto(UgovorODjeluDto ugovorODjeluDto) {
        UgovorODjeluDetailsDto ugovorODjeluDetailsDto = new UgovorODjeluDetailsDto();
        ugovorODjeluDetailsDto.setVrstaUgovora(ugovorODjeluDto.getRadniOdnos().getVrstaUgovora().getNaziv());
        ugovorODjeluDetailsDto.setRadniOdnos(ugovorODjeluDto.getRadniOdnos().getNaziv());
        ugovorODjeluDetailsDto.setRadnoMjesto(ugovorODjeluDto.getRadnoMjesto().getNaziv());
        ugovorODjeluDetailsDto.setDatumOd(ugovorODjeluDto.getDatumOd());
        ugovorODjeluDetailsDto.setDatumDo(ugovorODjeluDto.getDatumDo());
        ugovorODjeluDetailsDto.setBrutoIznos(ugovorODjeluDto.getBrutoIznos());
        ugovorODjeluDetailsDto.setStopaPausalnogPriznatogTroska(ugovorODjeluDto.getStopaPausalnogPriznatogTroska());
        return ugovorODjeluDetailsDto;
    }
}
