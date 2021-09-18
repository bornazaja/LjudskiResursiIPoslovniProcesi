package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.rekapitulacija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduKalkulator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduResultDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RekapitulacijaObracunaUgovoraORaduKalkulatorImpl implements RekapitulacijaObracunaUgovoraORaduKalkulator {

    @Autowired
    private ObracunUgovoraORaduKalkulator obracunUgovoraORaduKalkulator;

    @Override
    public RekapitualcijaObracunaUgovoraORaduResultDto getResult(Integer idObracunUgovoraORadu) {
        ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto = obracunUgovoraORaduKalkulator.getResultByIdObracunUgovora(idObracunUgovoraORadu);

        List<RekapitulacijaObracunaUgovoraORaduItemDto> rekapitulacijaObracunaUgovoraORaduItemsDto = new ArrayList<>();
        obracunUgovoraORaduResultDto.getIsplatneListeUgovoraORadu().forEach((isplatnaListaUgovoraORadu) -> {
            RekapitulacijaObracunaUgovoraORaduItemDto rekapitulacijaObracunaUgovoraORaduItemDto = new RekapitulacijaObracunaUgovoraORaduItemDto();
            rekapitulacijaObracunaUgovoraORaduItemDto.setImeIPrezimeZaposlenika(isplatnaListaUgovoraORadu.getZaposlenikBasicDetails().getImeIPrezime());
            rekapitulacijaObracunaUgovoraORaduItemDto.setUkupanTrosak(isplatnaListaUgovoraORadu.getUkupanTrosak());
            rekapitulacijaObracunaUgovoraORaduItemDto.setBruto(isplatnaListaUgovoraORadu.getBruto());
            rekapitulacijaObracunaUgovoraORaduItemDto.setDohodak(isplatnaListaUgovoraORadu.getDohodak());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosDavanjaNaPlacu(isplatnaListaUgovoraORadu.getDavanjeResultList().getUkupanIznosDavanjaNaPlacu());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosDavanjaIzPlace(isplatnaListaUgovoraORadu.getDavanjeResultList().getUkupanIznosDavanjaIzPlace());
            rekapitulacijaObracunaUgovoraORaduItemDto.setOsobniOdbitak(isplatnaListaUgovoraORadu.getOsobniOdbitak());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosPoreza(isplatnaListaUgovoraORadu.getIznosPoreza());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosPrireza(isplatnaListaUgovoraORadu.getIznosPrireza());
            rekapitulacijaObracunaUgovoraORaduItemDto.setPorezIPrirez(isplatnaListaUgovoraORadu.getPorezIPrirezUkupno());
            rekapitulacijaObracunaUgovoraORaduItemDto.setNeto(isplatnaListaUgovoraORadu.getNeto());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosDodataka(isplatnaListaUgovoraORadu.getDodatakResultList().getUkupanIznos());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosObustava(isplatnaListaUgovoraORadu.getObustavaResultList().getUkupanIznos());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosPrekovremenihRadova(isplatnaListaUgovoraORadu.getPrekovremeniRadResultList().getUkupanIznos());
            rekapitulacijaObracunaUgovoraORaduItemDto.setIznosZaIsplatu(isplatnaListaUgovoraORadu.getIznosZaIsplatu());
            rekapitulacijaObracunaUgovoraORaduItemsDto.add(rekapitulacijaObracunaUgovoraORaduItemDto);
        });

        Double ukupnoUkupanTrosak = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getUkupanTrosak()).sum();
        Double ukupanBruto = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getBruto()).sum();
        Double ukupanDohodak = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getDohodak()).sum();
        Double ukupanIznosDavanjaNaPlacu = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosDavanjaNaPlacu()).sum();
        Double ukupanIznosDavanjaIzPlace = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosDavanjaIzPlace()).sum();
        Double ukupanOsobniOdbitak = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getOsobniOdbitak()).sum();
        Double ukupanIznosPoreza = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosPoreza()).sum();
        Double ukupanIznosPrireza = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosPrireza()).sum();
        Double ukupanPorezIPrirez = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getPorezIPrirez()).sum();
        Double ukupanNeto = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getNeto()).sum();
        Double ukupanIznosDodataka = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosDodataka()).sum();
        Double ukupanIznosObustava = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosObustava()).sum();
        Double ukupanIznosPrekovremenihRadova = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosPrekovremenihRadova()).sum();
        Double ukupanIznosZaIsplatu = rekapitulacijaObracunaUgovoraORaduItemsDto.stream().mapToDouble(x -> x.getIznosZaIsplatu()).sum();

        RekapitualcijaObracunaUgovoraORaduResultDto rekapitualcijaObracunaUgovoraORaduResultDto = new RekapitualcijaObracunaUgovoraORaduResultDto();
        rekapitualcijaObracunaUgovoraORaduResultDto.setVrstaObracuna(obracunUgovoraORaduResultDto.getVrstaObracuna());
        rekapitualcijaObracunaUgovoraORaduResultDto.setOpis(obracunUgovoraORaduResultDto.getOpis());
        rekapitualcijaObracunaUgovoraORaduResultDto.setDatumObracuna(obracunUgovoraORaduResultDto.getDatumObracuna());
        rekapitualcijaObracunaUgovoraORaduResultDto.setValuta(obracunUgovoraORaduResultDto.getValuta());
        rekapitualcijaObracunaUgovoraORaduResultDto.setDatumOd(obracunUgovoraORaduResultDto.getDatumOd());
        rekapitualcijaObracunaUgovoraORaduResultDto.setDatumDo(obracunUgovoraORaduResultDto.getDatumDo());
        rekapitualcijaObracunaUgovoraORaduResultDto.setPodaciOTvrtki(obracunUgovoraORaduResultDto.getPodaciOTvrtki());
        rekapitualcijaObracunaUgovoraORaduResultDto.setRekapitulacijaObracunaUgovoraORaduItems(rekapitulacijaObracunaUgovoraORaduItemsDto);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupnoUkupanTrosak(ukupnoUkupanTrosak);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanBruto(ukupanBruto);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanDohodak(ukupanDohodak);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosDavanjaNaPlacu(ukupanIznosDavanjaNaPlacu);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosDavanjaIzPlace(ukupanIznosDavanjaIzPlace);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanOsobniOdbitak(ukupanOsobniOdbitak);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosPoreza(ukupanIznosPoreza);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosPrireza(ukupanIznosPrireza);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanPorezIPrirez(ukupanPorezIPrirez);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanNeto(ukupanNeto);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosDodataka(ukupanIznosDodataka);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosObustava(ukupanIznosObustava);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosPrekovremenihRadova(ukupanIznosPrekovremenihRadova);
        rekapitualcijaObracunaUgovoraORaduResultDto.setUkupanIznosZaIsplatu(ukupanIznosZaIsplatu);
        return rekapitualcijaObracunaUgovoraORaduResultDto;
    }
}
