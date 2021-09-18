package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellBorderStyle;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfFonts;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableHeaderOrientation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableListBeanConfiguration;
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
public class ObracunUgovoraORaduPdfReportImpl implements ObracunUgovoraORaduPdfReport {

    @Autowired
    private ObracunUgovoraORaduKalkulator obracunUgovoraORaduKalkulator;

    @Autowired
    private ObracunUgovoraCommonPdfReport obracunUgovoraCommonPdfReport;

    @Override
    public void createByIdObracunUgovora(Integer idObracunUgovora, String path) {
        ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto = obracunUgovoraORaduKalkulator.getResultByIdObracunUgovora(idObracunUgovora);
        create(obracunUgovoraORaduResultDto, path);
    }

    @Override
    public void createByIdIsplatnaLista(Integer idIsplatnaLista, String path) {
        ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto = obracunUgovoraORaduKalkulator.getResultByIdIsplatnaLista(idIsplatnaLista);
        create(obracunUgovoraORaduResultDto, path);
    }

    private void create(ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto, String path) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addContent(document, obracunUgovoraORaduResultDto);
            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addContent(Document document, ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto) throws DocumentException {
        PodaciOTvrtkiDto podaciOTvrtki = obracunUgovoraORaduResultDto.getPodaciOTvrtki();

        for (IsplatnaListaUgovoraORaduDto isplatnaListaUgovoraORaduDto : obracunUgovoraORaduResultDto.getIsplatneListeUgovoraORadu()) {
            Paragraph nazivTvrtkeParagraph = PdfUtils.createParagraph(podaciOTvrtki.getNaziv(), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);
            Paragraph adresaTvrtkeParagraph = PdfUtils.createParagraph(String.format("%s, %s", podaciOTvrtki.getUlica(), podaciOTvrtki.getGrad().getNaziv()), PdfFonts.SMALL_NORMAL, Element.ALIGN_LEFT);

            Paragraph naslovParagraph = PdfUtils.createParagraph("ISPLATNA LISTA UGOVORA O RADU", PdfFonts.LARGE_BOLD, Element.ALIGN_CENTER);

            Paragraph podaciOZaposlenikuParagraph = PdfUtils.createParagraph("Podaci o zaposleniku", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOZaposlenikuTable = obracunUgovoraCommonPdfReport.createPodaciOZaposlenikuTable(isplatnaListaUgovoraORaduDto.getZaposlenikBasicDetails());

            Paragraph podaciOUgovoruParagraph = PdfUtils.createParagraph("Podaci o ugovoru", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOUgovoruTable = createPodaciOUgovoruTable(isplatnaListaUgovoraORaduDto.getUgovorORaduDetails());

            Paragraph podaciOObracunuParagraph = PdfUtils.createParagraph("Podaci o obračunu", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable podaciOObracunuTable = createPodaciOObracunuTable(obracunUgovoraORaduResultDto);

            Paragraph davanjaParagraph = PdfUtils.createParagraph("Davanja", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable davanjaTable = obracunUgovoraCommonPdfReport.createDavanjaTable(isplatnaListaUgovoraORaduDto.getDavanjeResultList());

            Paragraph olaksiceParagraph = PdfUtils.createParagraph("Olakšice", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable olaksiceTable = createOlaksiceTable(isplatnaListaUgovoraORaduDto.getOlaksicaResultList());

            Paragraph dodatciParagraph = PdfUtils.createParagraph("Dodatci", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable dodatciTable = obracunUgovoraCommonPdfReport.createDodatciTable(isplatnaListaUgovoraORaduDto.getDodatakResultList());

            Paragraph obustaveParagraph = PdfUtils.createParagraph("Obustave", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable obustaveTable = obracunUgovoraCommonPdfReport.createObustaveTable(isplatnaListaUgovoraORaduDto.getObustavaResultList());

            Paragraph prekovremeniRadoviParagraph = PdfUtils.createParagraph("Prekovremeni radovi", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable prekovremeniRadoviTable = obracunUgovoraCommonPdfReport.createPrekovremeniRadoviTable(isplatnaListaUgovoraORaduDto.getPrekovremeniRadResultList());

            Paragraph detaljiIzracunaParagraph = PdfUtils.createParagraph("Detalji izračuna", PdfFonts.MEDIUM_BOLD, Element.ALIGN_LEFT);
            PdfPTable detaljiIzracunaTable = createDetaljiIzracunaTable(isplatnaListaUgovoraORaduDto);

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
            document.add(olaksiceParagraph);
            document.add(PdfUtils.createNewLineByParagraph());
            document.add(olaksiceTable);
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

    private PdfPTable createPodaciOUgovoruTable(UgovorORaduDetailsDto ugovorORaduDetailsDto) {
        List<String> displayNames = Arrays.asList("Vrsta ugovora", "Radni odnos", "Radno mjesto", "Datum od", "Datum do", "Broj radnih sati tjedno", "Bruto plaća");
        List<String> propertyNames = Arrays.asList("vrstaUgovora", "radniOdnos", "radnoMjesto", "datumOd", "datumDo", "brojRadnihSatiTjedno", "brutoPlaca");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, ugovorORaduDetailsDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }

    private PdfPTable createPodaciOObracunuTable(ObracunUgovoraORaduResultDto obracunUgovoraORaduResultDto) {
        List<String> displayNames = Arrays.asList("Vrsta obračuna", "Opis", "Datum obračuna", "Datum od", "Datum do", "Valuta", "Osnovni osobni odbitak", "Osnovica osobnog odbitka");
        List<String> propertyNames = Arrays.asList("vrstaObracuna.naziv", "opis", "datumObracuna", "datumOd", "datumDo", "valuta.naziv", "osnovniOsobniOdbitak", "osnovicaOsobnogOdbitka");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, obracunUgovoraORaduResultDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }

    private PdfPTable createOlaksiceTable(OlaksicaResultListDto olaksicaResultList) {
        List<String> displayNames = Arrays.asList("Naziv", "Osnovica", "Koeficjent", "Iznos");
        List<String> propertyNames = Arrays.asList("naziv", "osnovica", "koeficjent", "iznos");
        List<String> footerCells = Arrays.asList("Ukupno:", "", "", NumberUtils.format(olaksicaResultList.getUkupanIznos(), NumberFormatPatterns.DECIMAL_HR));

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, footerPdfCellConfiguration);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, olaksicaResultList.getOlaksiceDetailsDto(), PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), footerCells);
    }

    private PdfPTable createDetaljiIzracunaTable(IsplatnaListaUgovoraORaduDto isplatnaListaUgovoraORaduDto) {
        List<String> displayNames = new ListBuilder<>()
                .add("Ukupan trošak")
                .add("Ukupan iznos davanja na plaću")
                .add("Bruto")
                .add("Ukupan iznos davanja iz plaće")
                .add("Dohodak")
                .add("Osobni odbitak")
                .add("Ukupno porez")
                .add(String.format("Iznos poreza (%s)", NumberUtils.format(isplatnaListaUgovoraORaduDto.getStopaPoreza(), NumberFormatPatterns.PERCENTAGE)))
                .add(String.format("Iznos prireza (%s)", NumberUtils.format(isplatnaListaUgovoraORaduDto.getStopaPrireza(), NumberFormatPatterns.PERCENTAGE)))
                .add("Porez i prirez ukupno")
                .add("Neto")
                .add("Ukupan iznos dodataka")
                .add("Ukupan iznos obustava")
                .add("Ukupan iznos prekovremenih radova")
                .add("Iznos za isplatu")
                .build();

        List<String> propertyNames = new ListBuilder<>()
                .add("ukupanTrosak")
                .add("davanjeResultList.ukupanIznosDavanjaNaPlacu")
                .add("bruto")
                .add("davanjeResultList.ukupanIznosDavanjaIzPlace")
                .add("dohodak")
                .add("osobniOdbitak")
                .add("ukupnoPorez")
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

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, isplatnaListaUgovoraORaduDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }
}
