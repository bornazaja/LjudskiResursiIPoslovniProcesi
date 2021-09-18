package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.rekapitulacija;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellBorderStyle;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfFonts;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableListBeanConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberFormatPatterns;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RekapitulacijaObracunaUgovoraORaduPdfReportImpl implements RekapitulacijaObracunaUgovoraORaduPdfReport {

    @Autowired
    private RekapitulacijaObracunaUgovoraORaduKalkulator rekapitulacijaObracunaUgovoraORaduKalkulator;

    @Override
    public void create(Integer idObracunUgovora, String path) {
        RekapitualcijaObracunaUgovoraORaduResultDto rekapitualcijaObracunaUgovoraORaduResultDto = rekapitulacijaObracunaUgovoraORaduKalkulator.getResult(idObracunUgovora);
        create(rekapitualcijaObracunaUgovoraORaduResultDto, path);
    }

    private void create(RekapitualcijaObracunaUgovoraORaduResultDto rekapitualcijaObracunaUgovoraORaduResultDto, String location) {
        try {
            Document document = new Document(PageSize.A4_LANDSCAPE.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(location));
            document.open();
            addContent(document, rekapitualcijaObracunaUgovoraORaduResultDto);
            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addContent(Document document, RekapitualcijaObracunaUgovoraORaduResultDto rekapitualcijaObracunaUgovoraORaduResultDto) throws DocumentException {
        PodaciOTvrtkiDto podaciOTvrtkiDto = rekapitualcijaObracunaUgovoraORaduResultDto.getPodaciOTvrtki();

        Paragraph nazivTvrtkeParagraph = PdfUtils.createParagraph(podaciOTvrtkiDto.getNaziv(), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);
        Paragraph adresaTvrtkeParagraph = PdfUtils.createParagraph(String.format("%s, %s", podaciOTvrtkiDto.getUlica(), podaciOTvrtkiDto.getGrad().getNaziv()), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);

        int mjesec = rekapitualcijaObracunaUgovoraORaduResultDto.getDatumOd().getMonthValue();
        int godina = rekapitualcijaObracunaUgovoraORaduResultDto.getDatumOd().getYear();

        Paragraph naslovParagraph = PdfUtils.createParagraph(String.format("REKAPITULACIJA OBRAČUNA UGOVORA O RADU ZA %d. %d.", mjesec, godina), PdfFonts.LARGE_BOLD, Element.ALIGN_CENTER);

        PdfPTable rekapitulacijaTable = createRekapitulacijaTable(rekapitualcijaObracunaUgovoraORaduResultDto);

        Paragraph valutaInfoParagraph = PdfUtils.createParagraph(String.format("Svi iznosi su izraženi u valuti: %s", rekapitualcijaObracunaUgovoraORaduResultDto.getValuta().getNaziv()), PdfFonts.SMALL_BOLD, Element.ALIGN_CENTER);

        document.add(nazivTvrtkeParagraph);
        document.add(adresaTvrtkeParagraph);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(naslovParagraph);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(rekapitulacijaTable);
        document.add(PdfUtils.createNewLineByParagraph());
        document.add(valutaInfoParagraph);
        document.newPage();
    }

    private PdfPTable createRekapitulacijaTable(RekapitualcijaObracunaUgovoraORaduResultDto rekapitualcijaObracunaUgovoraORaduResultDto) {
        List<String> displayNames = new ListBuilder<>()
                .add("Ime i prezime")
                .add("Ukupan trošak")
                .add("Bruto")
                .add("Dohodak")
                .add("Iznos davanja na plaću")
                .add("Iznos davanja iz plaće")
                .add("Osobni odbitak")
                .add("Iznos poreza")
                .add("Iznos prireza")
                .add("Porez i prirez")
                .add("Neto")
                .add("Iznos dodataka")
                .add("Iznos obustave")
                .add("Iznos prekovremenih radova")
                .add("Iznos za isplatu")
                .build();

        List<String> propertyNames = new ListBuilder<>()
                .add("imeIPrezimeZaposlenika")
                .add("ukupanTrosak")
                .add("bruto")
                .add("dohodak")
                .add("iznosDavanjaNaPlacu")
                .add("iznosDavanjaIzPlace")
                .add("osobniOdbitak")
                .add("iznosPoreza")
                .add("iznosPrireza")
                .add("porezIPrirez")
                .add("neto")
                .add("iznosDodataka")
                .add("iznosObustava")
                .add("iznosPrekovremenihRadova")
                .add("iznosZaIsplatu")
                .build();

        List<String> footerCells = new ListBuilder<>()
                .add("Ukupno:")
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupnoUkupanTrosak(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanBruto(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanDohodak(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosDavanjaNaPlacu(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosDavanjaIzPlace(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanOsobniOdbitak(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosPoreza(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosPrireza(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanPorezIPrirez(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanNeto(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosDodataka(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosObustava(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosPrekovremenihRadova(), NumberFormatPatterns.DECIMAL_HR))
                .add(NumberUtils.format(rekapitualcijaObracunaUgovoraORaduResultDto.getUkupanIznosZaIsplatu(), NumberFormatPatterns.DECIMAL_HR))
                .build();

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.YELLOW, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOX, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, footerPdfCellConfiguration);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, rekapitualcijaObracunaUgovoraORaduResultDto.getRekapitulacijaObracunaUgovoraORaduItems(), PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), footerCells);
    }
}
