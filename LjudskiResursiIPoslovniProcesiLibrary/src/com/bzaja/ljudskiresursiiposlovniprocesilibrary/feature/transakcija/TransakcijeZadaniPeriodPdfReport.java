package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellBorderStyle;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfFonts;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableHeaderOrientation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableListBeanConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableSingleBeanConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.LocalDatePattern;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.LocalDateUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberFormatPatterns;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransakcijeZadaniPeriodPdfReport {

    @Autowired
    private TransakcijaService transakcijaService;

    public void create(LocalDate datumOd, LocalDate datumDo, String path) {
        TransakcijaResultListDto transakcijaResultListDto = transakcijaService.getTransakcijaResultList(datumOd, datumDo);
        create(transakcijaResultListDto, path);
    }

    private void create(TransakcijaResultListDto transakcijaResultListDto, String path) {
        try {
            Document document = new Document(PageSize.A4_LANDSCAPE.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addContent(document, transakcijaResultListDto);
            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addContent(Document document, TransakcijaResultListDto transakcijaResultListDto) throws DocumentException {
        PodaciOTvrtkiDto podaciOTvrtkiDto = transakcijaResultListDto.getPodaciOTvrtki();

        Paragraph nazivTvrtkeParagraph = PdfUtils.createParagraph(podaciOTvrtkiDto.getNaziv(), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);
        Paragraph adresaTrvtkeParagraph = PdfUtils.createParagraph(String.format("%s, %s", podaciOTvrtkiDto.getUlica(), podaciOTvrtkiDto.getGrad().getNaziv()), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);

        String datumOdStr = LocalDateUtils.format(transakcijaResultListDto.getDatumOd(), LocalDatePattern.HR);
        String datumDoStr = LocalDateUtils.format(transakcijaResultListDto.getDatumDo(), LocalDatePattern.HR);

        Paragraph naslovParagraph = PdfUtils.createParagraph(String.format("IZVJEŠTAJ O TRANSAKCIJAMA ZA PERIOD: %s - %s", datumOdStr, datumDoStr), PdfFonts.LARGE_BOLD, Element.ALIGN_CENTER);

        PdfPTable transakcijeTable = createTransakcijeTable(transakcijaResultListDto.getTransakcijaReportItems(), transakcijaResultListDto.getValuta());

        PdfPTable detaljiIzracunaTable = createDetaljiIzracunaTable(transakcijaResultListDto);

        Paragraph valutaInfoParagraph = PdfUtils.createParagraph(String.format("Svi iznosi su izraženi u valuti: %s", transakcijaResultListDto.getValuta().getNaziv()), PdfFonts.SMALL_BOLD, Element.ALIGN_CENTER);

        document.add(nazivTvrtkeParagraph);
        document.add(adresaTrvtkeParagraph);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(naslovParagraph);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(transakcijeTable);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(detaljiIzracunaTable);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(valutaInfoParagraph);
        document.newPage();
    }

    private PdfPTable createTransakcijeTable(List<TransakcijaReportItemDto> transakcijaReportItems, ValutaDto valuta) {
        List<String> displayProperties = new ListBuilder<>()
                .add("Poslovni partner")
                .add("Opis")
                .add("Iznos u orginalnoj valuti")
                .add("Valuta")
                .add(String.format("Iznos u domaćoj valuti (%s)", valuta.getNaziv()))
                .add("Vrsta transakcije")
                .add("Kategorija transakcije")
                .add("Datum transakcije")
                .build();

        List<String> nameProperties = new ListBuilder<>()
                .add("poslovniPartner.naziv")
                .add("opis")
                .add("iznosUOrginalnojValuti")
                .add("valuta.naziv")
                .add("iznosUDomacojValuti")
                .add("vrstaTransakcije.naziv")
                .add("kategorijaTransakcija.naziv")
                .add("datumTransakcije")
                .build();

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.YELLOW, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, null);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, transakcijaReportItems, PropertyInfoUtils.toPropertiesInfo(displayProperties, nameProperties), new ArrayList<>());
    }

    private PdfPTable createDetaljiIzracunaTable(TransakcijaResultListDto transakcijaResultListDto) {
        List<String> displayNames = new ListBuilder<>()
                .add("Ukupan iznos prihoda")
                .add("Ukupan iznos rashoda")
                .add("Profit prije poreza i prireza")
                .add(String.format("Iznos poreza (%s)", NumberUtils.format(transakcijaResultListDto.getStopaPoreza(), NumberFormatPatterns.PERCENTAGE)))
                .add(String.format("Iznos prireza (%s):", NumberUtils.format(transakcijaResultListDto.getStopaPrireza(), NumberFormatPatterns.PERCENTAGE)))
                .add("Porez i prirez ukupno")
                .add("Profit nakon poreza i prireza")
                .build();

        List<String> propertyNames = new ListBuilder<>()
                .add("ukupanIznosPrihoda")
                .add("ukupanIznosRashoda")
                .add("profitPrijePorezaIPrireza")
                .add("iznosPoreza")
                .add("iznosPrireza")
                .add("porezIPrirezUkupno")
                .add("profitNakonPorezaIPrireza")
                .build();

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.YELLOW, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_LEFT);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_RIGHT);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.VERTICAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, transakcijaResultListDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }
}
