package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadResultListDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikBasicDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellBorderStyle;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfCellConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfFonts;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableHeaderOrientation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableListBeanConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.PdfTableSingleBeanConfiguration;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberFormatPatterns;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ObracunUgovoraCommonPdfReportImpl implements ObracunUgovoraCommonPdfReport {

    @Override
    public PdfPTable createPodaciOZaposlenikuTable(ZaposlenikBasicDetailsDto zaposlenikBasicDetailsDto) {
        List<String> displayNames = Arrays.asList("Ime i prezime", "OIB", "Prebivalište");
        List<String> propertyNames = Arrays.asList("imeIPrezime", "oib", "prebivaliste");

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration = new PdfTableSingleBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, PdfTableHeaderOrientation.HORIZONTAL);

        return PdfTableMaker.make(pdfTableSingleBeanConfiguration, zaposlenikBasicDetailsDto, PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames));
    }

    @Override
    public PdfPTable createDavanjaTable(DavanjeResultListDto davanjeResultList) {
        List<String> displayNames = Arrays.asList("Naziv", "Osnovica", "Stopa na plaću", "Iznos na plaću", "Stopa iz plaće", "Iznos iz plaće");
        List<String> propertyNames = Arrays.asList("naziv", "osnovica", "stopaNaPlacu", "iznosNaPlacu", "stopaIzPlace", "iznosIzPlace");
        List<String> footerCells = Arrays.asList("Ukupno:", "", "", NumberUtils.format(davanjeResultList.getUkupanIznosDavanjaNaPlacu(), NumberFormatPatterns.DECIMAL_HR), "", NumberUtils.format(davanjeResultList.getUkupanIznosDavanjaIzPlace(), NumberFormatPatterns.DECIMAL_HR));

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, footerPdfCellConfiguration);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, davanjeResultList.getDavanjaDetailsDto(), PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), footerCells);
    }

    @Override
    public PdfPTable createDodatciTable(DodatakResultListDto dodatakResultList) {
        List<String> displayNames = Arrays.asList("Naziv", "Iznos");
        List<String> propertyNames = Arrays.asList("naziv", "iznos");
        List<String> footerCells = Arrays.asList("Ukupno:", NumberUtils.format(dodatakResultList.getUkupanIznos(), NumberFormatPatterns.DECIMAL_HR));

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, footerPdfCellConfiguration);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, dodatakResultList.getDodatciDetailsDto(), PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), footerCells);
    }

    @Override
    public PdfPTable createObustaveTable(ObustavaResultListDto obustavaResultList) {
        List<String> displayNames = Arrays.asList("Naziv", "Iznos");
        List<String> propertyNames = Arrays.asList("naziv", "iznos");
        List<String> footerCells = Arrays.asList("Ukupno:", NumberUtils.format(obustavaResultList.getUkupanIznos(), NumberFormatPatterns.DECIMAL_HR));

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, footerPdfCellConfiguration);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, obustavaResultList.getObustaveDetailsDto(), PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), footerCells);
    }

    @Override
    public PdfPTable createPrekovremeniRadoviTable(PrekovremeniRadResultListDto prekovremeniRadResultList) {
        List<String> displayNames = Arrays.asList("Naziv", "Koeficjent", "Broj dodatnih sati", "Iznos");
        List<String> propertyNames = Arrays.asList("naziv", "koeficjent", "brojDodatnihSati", "iznos");
        List<String> footerCells = Arrays.asList("Ukupno:", "", "", NumberUtils.format(prekovremeniRadResultList.getUkupanIznos(), NumberFormatPatterns.DECIMAL_HR));

        PdfCellConfiguration headerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration dataPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_NORMAL, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM, BaseColor.BLACK, Element.ALIGN_CENTER);
        PdfCellConfiguration footerPdfCellConfiguration = new PdfCellConfiguration(PdfFonts.SMALL_BOLD, BaseColor.WHITE, PdfCellBorderStyle.BOTTOM_TOP, BaseColor.BLACK, Element.ALIGN_CENTER);

        PdfTableListBeanConfiguration pdfTableListBeanConfiguration = new PdfTableListBeanConfiguration(Element.ALIGN_CENTER, 100f, headerPdfCellConfiguration, dataPdfCellConfiguration, footerPdfCellConfiguration);

        return PdfTableMaker.make(pdfTableListBeanConfiguration, prekovremeniRadResultList.getPrekovremeniRadoviDetailsDto(), PropertyInfoUtils.toPropertiesInfo(displayNames, propertyNames), footerCells);
    }
}
