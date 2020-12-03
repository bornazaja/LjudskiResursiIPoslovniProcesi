package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza.VrstePoreza;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije.VrsteTransakcija;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PayrollCalculatorUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransakcijeKalkulator {

    @Autowired
    private PorezService porezService;

    @Autowired
    private PodaciOTvrtkiService podaciOTvrtkiService;

    @Autowired
    private ValutaService valutaService;

    public TransakcijaResultListDto izracunaj(LocalDate datumOd, LocalDate datumDo, List<TransakcijaDto> transakcije) {

        List<TransakcijaReportItemDto> transakcijaReportItems = new ArrayList<>();

        transakcije.forEach((transakcija) -> {
            Double iznosUDomacojValuti = transakcija.getIznos() * transakcija.getSrednjiTecaj() / transakcija.getValuta().getJedinica();

            TransakcijaReportItemDto transakcijaReportItemDto = new TransakcijaReportItemDto();
            transakcijaReportItemDto.setPoslovniPartner(transakcija.getPoslovniPartner());
            transakcijaReportItemDto.setOpis(transakcija.getOpis());
            transakcijaReportItemDto.setIznosUOrginalnojValuti(transakcija.getIznos());
            transakcijaReportItemDto.setValuta(transakcija.getValuta());
            transakcijaReportItemDto.setIznosUDomacojValuti(iznosUDomacojValuti);
            transakcijaReportItemDto.setVrstaTransakcije(transakcija.getVrstaTransakcije());
            transakcijaReportItemDto.setKategorijaTransakcija(transakcija.getKategorijaTransakcije());
            transakcijaReportItemDto.setDatumTransakcije(transakcija.getDatumTransakcije());
            transakcijaReportItems.add(transakcijaReportItemDto);
        });

        PodaciOTvrtkiDto podaciOTvrtki = podaciOTvrtkiService.findFirst();

        Double ukupanIznosPrihoda = transakcijaReportItems.stream()
                .filter(x -> x.getVrstaTransakcije().getIdVrstaTransakcije().equals(VrsteTransakcija.PRIHOD.getId()))
                .mapToDouble(x -> x.getIznosUDomacojValuti()).sum();

        Double ukupanIznosRashoda = transakcijaReportItems.stream()
                .filter(x -> x.getVrstaTransakcije().getIdVrstaTransakcije().equals(VrsteTransakcija.RASHOD.getId()))
                .mapToDouble(x -> x.getIznosUDomacojValuti()).sum();

        Double profitPrijePorezaIPrireza = ukupanIznosPrihoda - ukupanIznosRashoda;

        Double stopaPoreza = porezService.findStopaByOsnovicaAndVrstaPorezaId(profitPrijePorezaIPrireza, VrstePoreza.POREZ_NA_DOBIT.getId());
        Double stopaPrireza = podaciOTvrtkiService.findFirst().getGrad().getPrirez();
        Double iznosPoreza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(profitPrijePorezaIPrireza, stopaPoreza);
        Double iznosPrireza = PayrollCalculatorUtils.fromStopaToIznosByOsnovica(iznosPoreza, stopaPrireza);
        Double porezIPrirezUkupno = iznosPoreza + iznosPrireza;
        Double profitNakonPorezaIPrireza = profitPrijePorezaIPrireza - porezIPrirezUkupno;
        ValutaDto valuta = valutaService.findByDrzaveJeDomovinaTrue();

        TransakcijaResultListDto transakcijaResultListDto = new TransakcijaResultListDto();
        transakcijaResultListDto.setPodaciOTvrtki(podaciOTvrtki);
        transakcijaResultListDto.setDatumOd(datumOd);
        transakcijaResultListDto.setDatumDo(datumDo);
        transakcijaResultListDto.setTransakcijaReportItems(transakcijaReportItems);
        transakcijaResultListDto.setUkupanIznosPrihoda(ukupanIznosPrihoda);
        transakcijaResultListDto.setUkupanIznosRashoda(ukupanIznosRashoda);
        transakcijaResultListDto.setProfitPrijePorezaIPrireza(profitPrijePorezaIPrireza);
        transakcijaResultListDto.setStopaPoreza(stopaPoreza);
        transakcijaResultListDto.setStopaPrireza(stopaPrireza);
        transakcijaResultListDto.setIznosPoreza(iznosPoreza);
        transakcijaResultListDto.setIznosPrireza(iznosPrireza);
        transakcijaResultListDto.setPorezIPrirezUkupno(porezIPrirezUkupno);
        transakcijaResultListDto.setProfitNakonPorezaIPrireza(profitNakonPorezaIPrireza);
        transakcijaResultListDto.setValuta(valuta);
        return transakcijaResultListDto;
    }
}
