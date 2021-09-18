package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaStudentskogUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonKalkulator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez.ObracunUgovoraPorezService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PayrollCalculatorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunStudentskihUgovoraKalkulatorImpl implements ObracunStudentskihUgovoraKalkulator {

    @Autowired
    private ObracunStudentskihUgovoraService obracunStudentskihUgovoraService;

    @Autowired
    private IsplatnaListaService isplatnaListaService;

    @Autowired
    private StudentskiUgovorService studentskiUgovorService;

    @Autowired
    private ObracunUgovoraPorezService obracunUgovoraPorezService;

    @Autowired
    private PodaciOTvrtkiService podaciOTvrtkiService;

    @Autowired
    private ObracunUgovoraCommonKalkulator obracunUgovoraCommonKalkulator;

    @Override
    public ObracunStudentskihUgovoraResultDto getResultByIdObracunUgovora(Integer idObracunUgovora) {
        ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto = obracunStudentskihUgovoraService.findById(idObracunUgovora);
        List<IsplatnaListaDto> isplatneListeDto = isplatnaListaService.findAllByIdObracunUgovora(idObracunUgovora);
        return getObracunStudentskihUgovoraResultDto(obracunStudentskihUgovoraDto, isplatneListeDto);
    }

    @Override
    public ObracunStudentskihUgovoraResultDto getResultByIdIsplatnaLista(Integer idIsplatnaLista) {
        Integer idObracunStudentskihUgovora = isplatnaListaService.findById(idIsplatnaLista).getObracunUgovora().getIdObracunUgovora();
        ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto = obracunStudentskihUgovoraService.findById(idObracunStudentskihUgovora);
        List<IsplatnaListaDto> isplatneListeDto = Arrays.asList(isplatnaListaService.findById(idIsplatnaLista));
        return getObracunStudentskihUgovoraResultDto(obracunStudentskihUgovoraDto, isplatneListeDto);
    }

    private ObracunStudentskihUgovoraResultDto getObracunStudentskihUgovoraResultDto(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto, List<IsplatnaListaDto> isplatneListeDto) {
        List<IsplatnaListaStudentskogUgovoraDto> isplatneListeStudentskihUgovoraDto = new ArrayList<>();

        isplatneListeDto.forEach((isplatnaLista) -> {
            isplatneListeStudentskihUgovoraDto.add(getIsplatnaListaStudentskogUgovora(obracunStudentskihUgovoraDto, isplatnaLista));
        });

        ObracunStudentskihUgovoraResultDto obracunStudentskihUgovoraResultDto = new ObracunStudentskihUgovoraResultDto();
        obracunStudentskihUgovoraResultDto.setVrstaObracuna(obracunStudentskihUgovoraDto.getVrstaObracuna());
        obracunStudentskihUgovoraResultDto.setOpis(obracunStudentskihUgovoraDto.getOpis());
        obracunStudentskihUgovoraResultDto.setDatumObracuna(obracunStudentskihUgovoraDto.getDatumObracuna());
        obracunStudentskihUgovoraResultDto.setValuta(obracunStudentskihUgovoraDto.getValuta());
        obracunStudentskihUgovoraResultDto.setPodaciOTvrtki(podaciOTvrtkiService.findFirst());
        obracunStudentskihUgovoraResultDto.setLimitGodisnjegIznosaZaStudenta(obracunStudentskihUgovoraDto.getLimitGodisnjegIznosaZaStudenta());
        obracunStudentskihUgovoraResultDto.setIsplatneListeStudentskihUgovora(isplatneListeStudentskihUgovoraDto);
        return obracunStudentskihUgovoraResultDto;
    }

    private IsplatnaListaStudentskogUgovoraDto getIsplatnaListaStudentskogUgovora(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto, IsplatnaListaDto isplatnaLista) {
        StudentskiUgovorDto studentskiUgovorDto = studentskiUgovorService.findById(isplatnaLista.getUgovor().getIdUgovor());

        Double neto = PayrollCalculatorUtils.getIznosByCijenaPoSatuIBrojOdradjenihSati(studentskiUgovorDto.getCijenaPoSatu(), studentskiUgovorDto.getBrojOdradjenihSati());

        DavanjeResultListDto davanjeResultListDto = obracunUgovoraCommonKalkulator.getDavanjeResultList(isplatnaLista.getIdIsplatnaLista(), neto);
        Double ukupniTrosak = neto + davanjeResultListDto.getUkupanIznosDavanjaNaPlacu();

        DodatakResultListDto dodatakResultListDto = obracunUgovoraCommonKalkulator.getDodatakResultList(isplatnaLista.getIdIsplatnaLista());
        ObustavaResultListDto obustavaResultListDto = obracunUgovoraCommonKalkulator.getObustavaResultList(isplatnaLista.getIdIsplatnaLista());
        PrekovremeniRadResultListDto prekovremeniRadResultListDto = obracunUgovoraCommonKalkulator.getPrekovremeniRadResultList(isplatnaLista.getIdIsplatnaLista(), studentskiUgovorDto.getCijenaPoSatu());

        Double ukupanIznosDodatakaObustavaIPrekovremenihRadova = dodatakResultListDto.getUkupanIznos() - obustavaResultListDto.getUkupanIznos() + prekovremeniRadResultListDto.getUkupanIznos();

        Double stopaPoreza = 0.0;
        Double stopaPrireza = 0.0;
        Double iznosPoreza = 0.0;
        Double iznosPrireza = 0.0;
        Double porezIPrirezUkupno = 0.0;

        if (studentskiUgovorDto.getDosadZaradjeniIznosUOvojGodini() > obracunStudentskihUgovoraDto.getLimitGodisnjegIznosaZaStudenta()) {
            stopaPoreza = obracunUgovoraPorezService.findStopaByObracunUgovoraId(obracunStudentskihUgovoraDto.getIdObracunUgovora());
            stopaPrireza = isplatnaLista.getPrirez();
            iznosPoreza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(neto, stopaPoreza);
            iznosPrireza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(iznosPoreza, stopaPrireza);
            porezIPrirezUkupno = iznosPoreza + iznosPrireza;
            neto = neto - porezIPrirezUkupno;
        }

        Double iznosZaIsplatu = neto + ukupanIznosDodatakaObustavaIPrekovremenihRadova;

        ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto = getZaposlenikBasicDetailsDto(studentskiUgovorDto, isplatnaLista);
        StudentskiUgovorDetailsDto studentskiUgovorDetailsDto = getStudentskiUgovorDetailsDto(studentskiUgovorDto);

        IsplatnaListaStudentskogUgovoraDto isplatnaListaStudentskogUgovoraDto = new IsplatnaListaStudentskogUgovoraDto();
        isplatnaListaStudentskogUgovoraDto.setZaposlenikBasicDetails(zaposlenikBasicDetailsDto);
        isplatnaListaStudentskogUgovoraDto.setStudentskiUgovorDetails(studentskiUgovorDetailsDto);
        isplatnaListaStudentskogUgovoraDto.setDavanjeResultList(davanjeResultListDto);
        isplatnaListaStudentskogUgovoraDto.setDodatakResultList(dodatakResultListDto);
        isplatnaListaStudentskogUgovoraDto.setObustavaResultList(obustavaResultListDto);
        isplatnaListaStudentskogUgovoraDto.setPrekovremeniRadResultList(prekovremeniRadResultListDto);
        isplatnaListaStudentskogUgovoraDto.setUkupanTrosak(ukupniTrosak);
        isplatnaListaStudentskogUgovoraDto.setStopaPoreza(stopaPoreza);
        isplatnaListaStudentskogUgovoraDto.setStopaPrireza(stopaPrireza);
        isplatnaListaStudentskogUgovoraDto.setIznosPoreza(iznosPoreza);
        isplatnaListaStudentskogUgovoraDto.setIznosPrireza(iznosPrireza);
        isplatnaListaStudentskogUgovoraDto.setPorezIPrirezUkupno(porezIPrirezUkupno);
        isplatnaListaStudentskogUgovoraDto.setNeto(neto);
        isplatnaListaStudentskogUgovoraDto.setIznosZaIsplatu(iznosZaIsplatu);
        return isplatnaListaStudentskogUgovoraDto;
    }

    private ZaposlenikBasicDetailsDto getZaposlenikBasicDetailsDto(StudentskiUgovorDto studentskiUgovorDto, IsplatnaListaDto isplatnaLista) {
        ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto = new ZaposlenikBasicDetailsDto();
        zaposlenikBasicDetailsDto.setImeIPrezime(String.format("%s %s", studentskiUgovorDto.getZaposlenik().getIme(), studentskiUgovorDto.getZaposlenik().getPrezime()));
        zaposlenikBasicDetailsDto.setOib(studentskiUgovorDto.getZaposlenik().getOib());
        zaposlenikBasicDetailsDto.setPrebivaliste(String.format("%s, %s", isplatnaLista.getPrebivaliste().getUlica(), isplatnaLista.getPrebivaliste().getGrad().getNaziv()));
        return zaposlenikBasicDetailsDto;
    }

    private StudentskiUgovorDetailsDto getStudentskiUgovorDetailsDto(StudentskiUgovorDto studentskiUgovorDto) {
        StudentskiUgovorDetailsDto studentskiUgovorDetailsDto = new StudentskiUgovorDetailsDto();
        studentskiUgovorDetailsDto.setVrstaUgovora(studentskiUgovorDto.getRadniOdnos().getVrstaUgovora().getNaziv());
        studentskiUgovorDetailsDto.setRadniOdnos(studentskiUgovorDto.getRadniOdnos().getNaziv());
        studentskiUgovorDetailsDto.setRadnoMjesto(studentskiUgovorDto.getRadnoMjesto().getNaziv());
        studentskiUgovorDetailsDto.setDatumOd(studentskiUgovorDto.getDatumOd());
        studentskiUgovorDetailsDto.setDatumDo(studentskiUgovorDto.getDatumDo());
        studentskiUgovorDetailsDto.setBrojOdradjenihSati(studentskiUgovorDto.getBrojOdradjenihSati());
        studentskiUgovorDetailsDto.setCijenaPoSatu(studentskiUgovorDto.getCijenaPoSatu());
        studentskiUgovorDetailsDto.setDosadZaradjeniIznosUOvojGodini(studentskiUgovorDto.getDosadZaradjeniIznosUOvojGodini());
        return studentskiUgovorDetailsDto;
    }
}
