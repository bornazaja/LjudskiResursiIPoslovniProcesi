package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellBorderStyle;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfFonts;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableHeaderOrientation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableSingleBeanConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberFormatPatterns;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraODjeluPdfReportImpl implements ObracunUgovoraODjeluPdfReport {

    @Autowired
    private ObracunUgovoraODjeluKalkulator obracunUgovoraODjeluKalkulator;

    @Autowired
    private ObracunUgovoraCommonPdfReport obracunUgovoraCommonPdfReport;

    @Override
    public void createByIdObracunUgovora(Integer idObracunUgovora, String path) {
        ObracunUgovoraODjeluResultDto obracunUgovoraODjeluResultDto = obracunUgovoraODjeluKalkulator.getResultByIdObracunUgovora(idObracunUgovora);
        create(obracunUgovoraODjeluResultDto, path);
    }

    @Override
    public void createByIdIsplatnaLista(Integer idIsplatnaLista, String path) {
        ObracunUgovoraODjeluResultDto obracunUgovoraODjeluResultDto = obracunUgovoraODjeluKalkulator.getResultByIdIsplatnaLista(idIsplatnaLista);
        create(obracunUgovoraODjeluResultDto, path);
    }

    private void create(ObracunUgovoraODjeluResultDto obracunUgovoraODjeluResultDto, String path) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addContent(document, obracunUgovoraODjeluResultDto);
            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addContent(Document document, ObracunUgovoraODjeluResultDto obracunUgovoraODjeluResultDto) throws DocumentException {
        PodaciOTvrtkiDto podaciOTvrtkiDto = obracunUgovoraODjeluResultDto.getPodaciOTvrtki();

        for (IsplatnaListaUgovoraODjeluDto isplatnaListaUgovoraODjeluDto : obracunUgovoraODjeluResultDto.getIsplatneListeUgovoraODjelu()) {

            Paragraph nazivTvrtkeParagraph = PdfUtils.createParagraph(podaciOTvrtkiDto.getNaziv(), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);
            Paragraph adresaTvrtkeParagraph = PdfUtils.createParagraph(String.format("%s, %s", podaciOTvrtkiDto.getUlica(), podaciOTvrtkiDto.getGrad().getNaziv()), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);

            Paragraph naslovParagraph = PdfUtils.createParagraph("ISPLATNA LISTA UGOVORA O DJELU", PdfFonts.LARGE_BOLD, Element.ALIGN_CENTER);

            Paragraph podaciOZaposlenikuParagraph = PdfUtils.createParagraph("Podaci o zaposleniku", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOZaposlenikuTable = obracunUgovoraCommonPdfReport.createPodaciOZaposlenikuTable(isplatnaListaUgovoraODjeluDto.getZaposlenikBasicDetails());

            Paragraph podaciOUgovoruParagraph = PdfUtils.createParagraph("Podaci o ugovoru", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOUgovoruTable = createPodaciOUgovoruTable(isplatnaListaUgovoraODjeluDto.getUgovorODjeluDetails());

            Paragraph podaciOObracunuParagraph = PdfUtils.createParagraph("Podaci o obračunu", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOObracunuTable = createPodaciOObracunuTable(obracunUgovoraODjeluResultDto);

            Paragraph davanjaParagraph = PdfUtils.createParagraph("Davanja", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable davanjaTable = obracunUgovoraCommonPdfReport.createDavanjaTable(isplatnaListaUgovoraODjeluDto.getDavanjeResultList());

            Paragraph dodatciParagraph = PdfUtils.createParagraph("Dodatci", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable dodatciTable = obracunUgovoraCommonPdfReport.createDodatciTable(isplatnaListaUgovoraODjeluDto.getDodatakResultList());

            Paragraph obustaveParagraph = PdfUtils.createParagraph("Obustave", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable obustaveTable = obracunUgovoraCommonPdfReport.createObustaveTable(isplatnaListaUgovoraODjeluDto.getObustavaResultList());

            Paragraph detaljiIzracunaParagraph = PdfUtils.createParagraph("Detalji izračuna", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable detaljiIzracunaTable = createDetaljiIzracunaTable(isplatnaListaUgovoraODjeluDto);

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
            document.add(detaljiIzracunaParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(detaljiIzracunaTable);
            document.newPage();
        }
    }

    private PdfPTable createPodaciOUgovoruTable(UgovorODjeluDetailsDto ugovorODjeluDetails) {
        List<String> displayNames = Arrays.asList("Vrsta ugovora", "Radni odnos", "Radno mjesto", "Datum od", "Datum do", "Bruto iznos", "Stopa paušalnog priznatog troška");
        List<String> propertyNames = Arrays.asList("vrstaUgovora", "radniOdnos", "radnoMjesto", "datumOd", "datumDo", "brutoIznos", "stopaPausalnogPriznatogTroska");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, ugovorODjeluDetails, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }

    private PdfPTable createPodaciOObracunuTable(ObracunUgovoraODjeluResultDto obracunUgovoraODjeluResultDto) {
        List<String> displayNames = Arrays.asList("Vrsta obračuna", "Opis", "Datum obračuna", "Valuta");
        List<String> propertyNames = Arrays.asList("vrstaObracuna.naziv", "opis", "datumObracuna", "valuta.naziv");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, obracunUgovoraODjeluResultDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }

    private PdfPTable createDetaljiIzracunaTable(IsplatnaListaUgovoraODjeluDto isplatnaListaUgovoraODjeluDto) {
        List<String> displayNames = new ListBuilder<>()
                .add(String.format("Iznos paušalnog priznatog troška (%s)", NumberUtils.format(isplatnaListaUgovoraODjeluDto.getUgovorODjeluDetails().getStopaPausalnogPriznatogTroska(), NumberFormatPatterns.PERCENTAGE)))
                .add("Ukupan trošak")
                .add("Ukupan iznos davanja na plaću")
                .add("Bruto")
                .add("Ukupuan iznos davanja iz plaće")
                .add("Dohodak")
                .add("Ukupno porez")
                .add(String.format("Iznos poreza (%s)", NumberUtils.format(isplatnaListaUgovoraODjeluDto.getStopaPoreza(), NumberFormatPatterns.PERCENTAGE)))
                .add(String.format("Iznos prireza (%s)", NumberUtils.format(isplatnaListaUgovoraODjeluDto.getStopaPrireza(), NumberFormatPatterns.PERCENTAGE)))
                .add("Porez i prirez ukupno")
                .add("Neto")
                .add("Ukupan iznos dodataka")
                .add("Ukupan iznos obustava")
                .add("Iznos za isplatu")
                .build();

        List<String> propertyNames = new ListBuilder<>()
                .add("iznosPausalnogPriznatogTroska")
                .add("ukupanTrosak")
                .add("davanjeResultList.ukupanIznosDavanjaNaPlacu")
                .add("bruto")
                .add("davanjeResultList.ukupanIznosDavanjaIzPlace")
                .add("dohodak")
                .add("ukupnoPorez")
                .add("iznosPoreza")
                .add("iznosPrireza")
                .add("porezIPrirezUkupno")
                .add("neto")
                .add("dodatakResultList.ukupanIznos")
                .add("obustavaResultList.ukupanIznos")
                .add("iznosZaIsplatu")
                .build();

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_LEFT);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_RIGHT);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.VERTICAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, isplatnaListaUgovoraODjeluDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }
}
