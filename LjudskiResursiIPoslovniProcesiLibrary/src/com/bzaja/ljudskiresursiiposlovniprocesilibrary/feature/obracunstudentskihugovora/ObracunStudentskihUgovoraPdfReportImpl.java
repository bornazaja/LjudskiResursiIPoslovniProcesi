package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaStudentskogUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDetailsDto;
import com.bzaja.myjavalibrary.pdf.PdfCellBorderStyle;
import com.bzaja.myjavalibrary.pdf.PdfCellConfiguration;
import com.bzaja.myjavalibrary.pdf.PdfFonts;
import com.bzaja.myjavalibrary.pdf.PdfTableHeaderOrientation;
import com.bzaja.myjavalibrary.pdf.PdfTableMaker;
import com.bzaja.myjavalibrary.pdf.PdfTableSingleBeanConfiguration;
import com.bzaja.myjavalibrary.pdf.PdfUtils;
import com.bzaja.myjavalibrary.util.LanguageFormat;
import com.bzaja.myjavalibrary.util.ListBuilder;
import com.bzaja.myjavalibrary.util.NumberFormatPatterns;
import com.bzaja.myjavalibrary.util.NumberUtils;
import com.bzaja.myjavalibrary.util.PropertyInfoUtils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunStudentskihUgovoraPdfReportImpl implements ObracunStudentskihUgovoraPdfReport {

    @Autowired
    private ObracunStudentskihUgovoraKalkulator obracunStudentskihUgovoraKalkulator;

    @Autowired
    private ObracunUgovoraCommonPdfReport obracunUgovoraCommonPdfReport;

    @Override
    public void createByIdObracunUgovora(Integer idObracunUgovora, String path) {
        ObracunStudentskihUgovoraResultDto obracunStudentskihUgovoraResultDto = obracunStudentskihUgovoraKalkulator.getResultByIdObracunUgovora(idObracunUgovora);
        create(obracunStudentskihUgovoraResultDto, path);
    }

    @Override
    public void createByIdIsplatnaLista(Integer idIsplatnaLista, String path) {
        ObracunStudentskihUgovoraResultDto obracunStudentskihUgovoraResultDto = obracunStudentskihUgovoraKalkulator.getResultByIdIsplatnaLista(idIsplatnaLista);
        create(obracunStudentskihUgovoraResultDto, path);
    }

    private void create(ObracunStudentskihUgovoraResultDto obracunStudentskihUgovoraResultDto, String path) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addContent(document, obracunStudentskihUgovoraResultDto);
            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addContent(Document document, ObracunStudentskihUgovoraResultDto obracunStudentskihUgovoraResultDto) throws DocumentException {
        PodaciOTvrtkiDto podaciOTvrtkiDto = obracunStudentskihUgovoraResultDto.getPodaciOTvrtki();

        for (IsplatnaListaStudentskogUgovoraDto isplatnaListaStudentskogUgovoraDto : obracunStudentskihUgovoraResultDto.getIsplatneListeStudentskihUgovora()) {
            Paragraph nazivTvrtkeParagraph = PdfUtils.createParagraph(podaciOTvrtkiDto.getNaziv(), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);
            Paragraph adresaTvrtkeParagraph = PdfUtils.createParagraph(String.format("%s, %s", podaciOTvrtkiDto.getUlica(), podaciOTvrtkiDto.getGrad().getNaziv()), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);

            Paragraph naslovParagraph = PdfUtils.createParagraph("ISPLATNA LISTA ZA STUDENTSKE UGOVORE", PdfFonts.LARGE_BOLD, Element.ALIGN_CENTER);

            Paragraph podaciOZaposlenikuParagraph = PdfUtils.createParagraph("Podaci o zaposleniku", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOZaposlenikuTable = obracunUgovoraCommonPdfReport.createPodaciOZaposlenikuTable(isplatnaListaStudentskogUgovoraDto.getZaposlenikBasicDetails());

            Paragraph podaciOUgovoruParagraph = PdfUtils.createParagraph("Podaci o ugovoru", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOUgovoruTable = createPodaciOUgovoruTable(isplatnaListaStudentskogUgovoraDto.getStudentskiUgovorDetails());

            Paragraph podaciOObracunuParagraph = PdfUtils.createParagraph("Podaci o obra??unu", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOObracunuTable = createPodaciOObracunuTable(obracunStudentskihUgovoraResultDto);

            Paragraph davanjaParagraph = PdfUtils.createParagraph("Davanja", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable davanjaTable = obracunUgovoraCommonPdfReport.createDavanjaTable(isplatnaListaStudentskogUgovoraDto.getDavanjeResultList());

            Paragraph dodatciParagraph = PdfUtils.createParagraph("Dodatci", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable dodatciTable = obracunUgovoraCommonPdfReport.createDodatciTable(isplatnaListaStudentskogUgovoraDto.getDodatakResultList());

            Paragraph obustaveParagraph = PdfUtils.createParagraph("Obustave", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable obustaveTable = obracunUgovoraCommonPdfReport.createObustaveTable(isplatnaListaStudentskogUgovoraDto.getObustavaResultList());

            Paragraph prekovremeniRadoviParagraph = PdfUtils.createParagraph("Prekovremeni radovi", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable prekovremeniRadoviTable = obracunUgovoraCommonPdfReport.createPrekovremeniRadoviTable(isplatnaListaStudentskogUgovoraDto.getPrekovremeniRadResultList());

            Paragraph detaljiIzracunaParagraph = PdfUtils.createParagraph("Detalji izra??una", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable detaljiIzracunaTable = createDetaljiIzracunaTable(isplatnaListaStudentskogUgovoraDto);

            document.add(nazivTvrtkeParagraph);
            document.add(adresaTvrtkeParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(naslovParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(podaciOZaposlenikuParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(podaciOZaposlenikuTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(podaciOUgovoruParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(podaciOUgovoruTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(podaciOObracunuParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(podaciOObracunuTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(davanjaParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(davanjaTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(dodatciParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(dodatciTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(obustaveParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(obustaveTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(prekovremeniRadoviParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(prekovremeniRadoviTable);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(detaljiIzracunaParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(detaljiIzracunaTable);
            document.newPage();
        }
    }

    private PdfPTable createPodaciOUgovoruTable(StudentskiUgovorDetailsDto studentskiUgovorDetails) {
        List<String> displayNames = Arrays.asList("Vrsta ugovora", "Radni odnos", "Radno mjesto", "Datum od", "Datum do", "Broj odra??enih sati", "Cijena po satu", "Dosad zara??eni iznos u ovoj godini");
        List<String> propertyNames = Arrays.asList("vrstaUgovora", "radniOdnos", "radnoMjesto", "datumOd", "datumDo", "brojOdradjenihSati", "cijenaPoSatu", "dosadZaradjeniIznosUOvojGodini");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, studentskiUgovorDetails, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), LanguageFormat.HR);
    }

    private PdfPTable createPodaciOObracunuTable(ObracunStudentskihUgovoraResultDto obracunStudentskihUgovoraResultDto) {
        List<String> displayNames = Arrays.asList("Vrsta obra??una", "Opis", "Datum obra??una", "Limit godi??njeg iznosa za studenta", "Valuta");
        List<String> propertyNames = Arrays.asList("vrstaObracuna.naziv", "opis", "datumObracuna", "limitGodisnjegIznosaZaStudenta", "valuta.naziv");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, footerPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, obracunStudentskihUgovoraResultDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), LanguageFormat.HR);
    }

    private PdfPTable createDetaljiIzracunaTable(IsplatnaListaStudentskogUgovoraDto isplatnaListaStudentskogUgovoraDto) {
        List<String> displayNames = new ListBuilder<>()
                .add("Ukupan tro??ak")
                .add("Ukupan iznos davanja na pla??u")
                .add(String.format("Iznos poreza (%s)", NumberUtils.format(isplatnaListaStudentskogUgovoraDto.getStopaPoreza(), NumberFormatPatterns.PERCENTAGE)))
                .add(String.format("Iznos prireza (%s)", NumberUtils.format(isplatnaListaStudentskogUgovoraDto.getStopaPrireza(), NumberFormatPatterns.PERCENTAGE)))
                .add("Porez i prirez ukupno")
                .add("Neto")
                .add("Ukupan iznos dodataka")
                .add("Ukupan iznos obustava")
                .add("Ukupan iznos prekovremenih raodva")
                .add("Iznos za isplatu")
                .build();

        List<String> propertyNames = new ListBuilder<>()
                .add("ukupanTrosak")
                .add("davanjeResultList.ukupanIznosDavanjaNaPlacu")
                .add("iznosPoreza")
                .add("iznosPrireza")
                .add("porezIPrirezUkupno")
                .add("neto")
                .add("dodatakResultList.ukupanIznos")
                .add("obustavaResultList.ukupanIznos")
                .add("prekovremeniRadResultList.ukupanIznos")
                .add("iznosZaIsplatu")
                .build();

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_LEFT);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_RIGHT);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.VERTICAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, isplatnaListaStudentskogUgovoraDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), LanguageFormat.HR);
    }
}
